package ru.hokan.flight_animation_project.ui.main.flight.process

import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.Point
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

// all these singleton Kotlin objects can and should be implemented as Dagger2-injected classes
// but for sake of variety I chose to use plain singleton classes
object AnimationsCalculator {

    private const val POINTS_IN_PATH = 30
    private const val REVERSE_PLANE_IMAGE_ROTATION_ANGLE = 90f

    fun getPlanePaths(
        googleMap: GoogleMap,
        departureCityScreenLocation: Point,
        midPointX: Float,
        arrivalCityScreenLocation: Point,
        movingWest: Boolean,
        planeImageWidth: Int,
        planeImageHeight: Int
    ): PathsHolder {
        // cause android is applying transformation upon image
        // based on its top-left corner we have to modify future
        // plane's path-on-screen positions so it will look alike
        // it's moving through its center, not corner
        val departurePlaneScreenLocation = Point(
            departureCityScreenLocation.x - planeImageWidth / 2,
            departureCityScreenLocation.y - planeImageHeight / 2
        )
        val arrivalPlaneScreenLocation = Point(
            arrivalCityScreenLocation.x - planeImageWidth / 2,
            arrivalCityScreenLocation.y - planeImageHeight / 2
        )
        val transitionPath = Path().apply {
            moveTo(
                departurePlaneScreenLocation.x.toFloat(),
                departurePlaneScreenLocation.y.toFloat()
            )
            cubicTo(
                midPointX, departurePlaneScreenLocation.y.toFloat(),
                midPointX, arrivalPlaneScreenLocation.y.toFloat(),
                arrivalPlaneScreenLocation.x.toFloat(), arrivalPlaneScreenLocation.y.toFloat()
            )
        }

        val (planePathLocations, rotationPath) = getPlanePathLocationsAndRotationPath(
            googleMap,
            transitionPath,
            movingWest,
            planeImageWidth,
            planeImageHeight
        )
        return PathsHolder(transitionPath, planePathLocations, rotationPath)
    }

    private fun getPlanePathLocationsAndRotationPath(
        googleMap: GoogleMap,
        transitionPath: Path,
        movingWest: Boolean,
        planeImageWidth: Int,
        planeImageHeight: Int
    ): Pair<List<LatLng>, FloatArray> {
        val (latLngPoints, floatPoints) =
            createPlanePathLocations(
                googleMap, transitionPath, planeImageWidth,
                planeImageHeight
            )
        val vectors = createVectorList(floatPoints)
        val rotationPath = createRotationPath(vectors, movingWest)
        return Pair(latLngPoints, rotationPath)
    }

    private fun createRotationPath(
        vectors: MutableList<Point>,
        movingWest: Boolean
    ): FloatArray {
        val maxX = vectors.maxBy { it.x }!!.x
        val maxY = vectors.maxBy { it.y }!!.y
        return vectors.map {
            val x = it.x.toFloat() - maxX
            val y = it.y.toFloat() - maxY
            // see 'FlightProcessFragment.setupPlaneAnimation'
            if (movingWest) {
                -(x + y) + REVERSE_PLANE_IMAGE_ROTATION_ANGLE
            } else {
                x + y
            }
        }.toFloatArray()
    }

    private fun createPlanePathLocations(
        googleMap: GoogleMap,
        path: Path,
        planeImageWidth: Int,
        planeImageHeight: Int
    ): Pair<MutableList<LatLng>, MutableList<Point>> {
        val projection = googleMap.projection
        val pathMeasure = PathMeasure(path, false)
        val speed = pathMeasure.length / POINTS_IN_PATH
        val latLngPoints = mutableListOf<LatLng>()
        val position = FloatArray(2)
        val floatPoints = mutableListOf<Point>()
        var distance = 0f
        while (distance < pathMeasure.length) {
            pathMeasure.getPosTan(distance, position, null)
            val point = Point(
                position[0].toInt(),
                position[1].toInt()
            )

            floatPoints.add(point)
            latLngPoints.add(
                // since we subtracted plane image dimensions from
                // original plane android Path, so that image will looks like
                // it's moving through the center of its own
                // to create dotted path line (which is independent from plane image rendering)
                // we have to add plane image sizes back
                projection.fromScreenLocation(
                    Point(
                        point.x + planeImageWidth / 2,
                        point.y + planeImageHeight / 2
                    )
                )
            )
            distance += speed
        }
        return Pair(latLngPoints, floatPoints)
    }

    private fun createVectorList(floatPoints: MutableList<Point>): MutableList<Point> {
        val vectors = mutableListOf<Point>()
        (1 until floatPoints.size).forEach {
            vectors.add(
                Point(
                    floatPoints[it].x - floatPoints[it - 1].x,
                    floatPoints[it].y - floatPoints[it - 1].y
                )
            )
        }
        return vectors
    }

}