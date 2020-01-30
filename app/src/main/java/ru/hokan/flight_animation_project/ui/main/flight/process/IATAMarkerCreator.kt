package ru.hokan.flight_animation_project.ui.main.flight.process

import android.graphics.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import ru.hokan.flight_animation_project.data.dto.City
import ru.hokan.flight_animation_project.ui.main.city.selection.CityType

object IATAMarkerCreator {

    private const val TEXT_SIZE = 50f
    private const val RECTANGLE_SIZE = 300f
    private const val ALPHA_VALUE = 135
    private const val BORDER_RADIUS_SIZE = 20f

    fun createIATAMarker(location: LatLng, iataName: String, cityType: CityType): MarkerOptions {
        val bitmap = Bitmap.createBitmap(
            RECTANGLE_SIZE.toInt(),
            RECTANGLE_SIZE.toInt(),
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawRectangle(canvas, iataName, cityType)
        drawIATAName(canvas, iataName)

        return MarkerOptions()
            .position(location)
            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
    }

    private fun drawRectangle(
        canvas: Canvas,
        text: String,
        cityType: CityType
    ) {
        val paint = Paint().apply {
            color = when (cityType) {
                CityType.ARRIVAL -> Color.RED
                CityType.DEPARTURE -> Color.BLUE
            }
            style = Paint.Style.FILL
            alpha = ALPHA_VALUE
            this.textSize = TEXT_SIZE
        }

        val borderSize = BORDER_RADIUS_SIZE
        val textWidth = paint.measureText(text) / 2
        val rect = RectF(
            RECTANGLE_SIZE / 2 - textWidth,
            RECTANGLE_SIZE - TEXT_SIZE - TEXT_SIZE / 2,
            RECTANGLE_SIZE,
            RECTANGLE_SIZE
        )
        canvas.drawRoundRect(rect, borderSize, borderSize, paint)
    }

    private fun drawIATAName(canvas: Canvas, text: String) {
        val paint = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.FILL
            this.textSize = TEXT_SIZE
            textAlign = Paint.Align.CENTER
        }

        val textWidth = paint.measureText(text)
        canvas.drawText(text, RECTANGLE_SIZE - textWidth, RECTANGLE_SIZE - TEXT_SIZE / 2, paint)
    }
}