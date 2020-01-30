package ru.hokan.flight_animation_project.ui.main.flight.process

import com.google.android.gms.maps.model.LatLngBounds
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

// plainly took from https://stackoverflow.com/questions/14631334/android-maps-v2-newlatlngbounds-with-bearing
object GoogleMapZoomCalculator {
    private const val WORLD_PX_HEIGHT = 256
    private const val WORLD_PX_WIDTH = 256
    private const val ZOOM_MAX = 21.0

    fun getBoundsZoomLevel(
        bounds: LatLngBounds,
        mapWidthPx: Int,
        mapHeightPx: Int,
        density: Float
    ): Double {

        val ne = bounds.northeast
        val sw = bounds.southwest

        val latFraction = (latRad(ne.latitude) - latRad(sw.latitude)) / Math.PI

        val lngDiff = ne.longitude - sw.longitude
        val lngFraction = (if (lngDiff < 0) lngDiff + 360 else lngDiff) / 360

        val realPxHeight = WORLD_PX_HEIGHT * density
        val realPxWidth = WORLD_PX_WIDTH * density

        val latZoom = zoom(mapHeightPx, realPxHeight, latFraction)
        val lngZoom = zoom(mapWidthPx, realPxWidth, lngFraction)

        val result = min(latZoom, lngZoom)
        return min(result, ZOOM_MAX)
    }

    private fun latRad(lat: Double): Double {
        val sin = sin(lat * Math.PI / 180)
        val radX2 = ln((1 + sin) / (1 - sin)) / 2
        return max(min(radX2, Math.PI), -Math.PI) / 2
    }

    private fun zoom(mapPx: Int, worldPx: Float, fraction: Double): Double {
        return ln(mapPx.toDouble() / worldPx.toDouble() / fraction) / ln(2f)
    }
}