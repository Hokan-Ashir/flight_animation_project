package ru.hokan.flight_animation_project.ui.main.flight.process

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.flight_animation_project.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.flight_process_fragment.*
import ru.hokan.flight_animation_project.ui.main.city.selection.CityType
import ru.hokan.flight_animation_project.ui.main.flight.path.selection.FlightPathSelectionViewModel
import ru.hokan.flight_animation_project.ui.main.flight.process.AnimationsCalculator.getPlanePaths
import kotlin.math.abs


class FlightProcessFragment : Fragment() {

    companion object {
        const val DEFAULT_ANIMATION_TIME_MS = 5_000L
        // despite using workaround for GoogleMapV2 API for calculating zoom
        // as well as LITE_MODE, it's advisable to add some padding to the map
        // decreasing whole zoom level so that bound markers (IATAs) gonna be visible
        const val ZOOM_PADDING = 0.2f
        const val PLANE_PATH_DOT_WIDTH = 15f
        const val PLANE_PATH_DOT_PATTERN_GAP = 20f
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.flight_process_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mapView.onCreate(savedInstanceState)
        mapView.visibility = View.VISIBLE

        mapView.getMapAsync {
            it.uiSettings.setAllGesturesEnabled(false)

            val model =
                ViewModelProviders.of(activity!!).get(FlightPathSelectionViewModel::class.java)
            val departureCity = model.departureCity.value!!
            val arrivalCity = model.arrivalCity.value!!
            val departureCityLocation = LatLng(departureCity.latitude!!, departureCity.longitude!!)
            val arrivalCityLocation = LatLng(arrivalCity.latitude!!, arrivalCity.longitude!!)

            setupCameraPositionAndZoom(arrivalCityLocation, departureCityLocation, it)
            createDepartureArrivalMarkers(
                it,
                departureCityLocation,
                arrivalCityLocation,
                departureCity.iata!!,
                arrivalCity.iata!!
            )
            setupPlaneAnimation(it, departureCityLocation, arrivalCityLocation)
        }
    }

    private fun setupCameraPositionAndZoom(
        arrivalCityLocation: LatLng,
        departureCityLocation: LatLng,
        it: GoogleMap
    ) {
        val builder = LatLngBounds.Builder()
        val bounds = builder.include(arrivalCityLocation).include(departureCityLocation).build()
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val density = resources.displayMetrics.density

        val boundsZoomLevel =
            GoogleMapZoomCalculator.getBoundsZoomLevel(
                bounds,
                width,
                height,
                density
            ) - ZOOM_PADDING
        val cameraPositionBuilder = CameraPosition.Builder()
        val cameraPosition =
            cameraPositionBuilder.zoom(boundsZoomLevel.toFloat()).target(bounds.center).build()
        it.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun setupPlaneAnimation(
        googleMap: GoogleMap,
        departureCityLocation: LatLng,
        arrivalCityLocation: LatLng
    ) {
        val projection = googleMap.projection
        val departureCityScreenLocation = projection.toScreenLocation(departureCityLocation)
        val arrivalCityScreenLocation = projection.toScreenLocation(arrivalCityLocation)

        plane.x = departureCityScreenLocation.x.toFloat()
        plane.y = departureCityScreenLocation.y.toFloat()

        // in case plane is moving west, it needs to be additionally rotated, cause original
        // image orientation towards east
        val longitudeDifference = departureCityLocation.longitude - arrivalCityLocation.longitude
        val movingWest =
            (longitudeDifference > 0 && abs(longitudeDifference) < 180)
                    || (abs(longitudeDifference) > 180 && longitudeDifference < 0)
        val midPointX =
            ((arrivalCityScreenLocation.x + departureCityScreenLocation.x) / 2).toFloat()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            applyBezierAnimation(
                googleMap,
                departureCityScreenLocation,
                midPointX,
                arrivalCityScreenLocation,
                movingWest
            )
        } else {
            // simply do not make app run on OS less than LOLLIPOP (see gradle.build)
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun applyBezierAnimation(
        googleMap: GoogleMap,
        departureCityScreenLocation: Point,
        midPointX: Float,
        arrivalCityScreenLocation: Point,
        movingWest: Boolean
    ) {

        val planePaths = getPlanePaths(
            googleMap,
            departureCityScreenLocation,
            midPointX,
            arrivalCityScreenLocation,
            movingWest,
            plane.width,
            plane.height
        )

        drawPlanePath(planePaths, googleMap)
        invokePlaneAnimations(planePaths)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun invokePlaneAnimations(planePaths: PathsHolder) {
        val transitionAnimator =
            ObjectAnimator.ofFloat(plane, View.X, View.Y, planePaths.transitionPath)
        val rotationAnimator =
            ObjectAnimator.ofFloat(plane, View.ROTATION, *planePaths.rotationPath)
        val animatorSet = AnimatorSet().setDuration(DEFAULT_ANIMATION_TIME_MS)
        animatorSet.playTogether(
            transitionAnimator,
            rotationAnimator
        )
        animatorSet.start()
    }

    private fun drawPlanePath(
        planePaths: PathsHolder,
        googleMap: GoogleMap
    ) {
        val polyLine = PolylineOptions()
            .pattern(listOf(Dot(), Gap(PLANE_PATH_DOT_PATTERN_GAP)))
            .addAll(planePaths.planePathCoordinates)
            .color(Color.RED)
            .jointType(JointType.ROUND)
            .width(PLANE_PATH_DOT_WIDTH)
        googleMap.addPolyline(polyLine)
    }

    private fun createDepartureArrivalMarkers(
        googleMap: GoogleMap,
        departureCityPosition: LatLng,
        arrivalCityPosition: LatLng,
        departureIATA: String,
        arrivalIATA: String
    ) {
        googleMap.addMarker(
            IATAMarkerCreator.createIATAMarker(
                departureCityPosition,
                departureIATA,
                CityType.DEPARTURE
            )
        )
        googleMap.addMarker(
            IATAMarkerCreator.createIATAMarker(
                arrivalCityPosition,
                arrivalIATA,
                CityType.ARRIVAL
            )
        )

    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
