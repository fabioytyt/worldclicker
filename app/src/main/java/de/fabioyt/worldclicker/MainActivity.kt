package de.fabioyt.worldclicker

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import de.fabioyt.worldclicker.ui.theme.WorldclickerTheme
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorldclickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                            name = "Android",
                            modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

}
fun createMarker(lat:Double, lng:Double, map: MapView, icon:Int) {
    val firstMarker = Marker(map)
    firstMarker.position = GeoPoint(lat, lng)
    firstMarker.icon = ContextCompat.getDrawable(map.context, icon)
//  firstMarker.title = "moin"
    map.overlays.add(firstMarker)
// "Invalidating" the map displays the marker as soon as it has been added.
    map.invalidate()
}
fun generateRandomCars(map: MapView, currentPos: GeoPoint) {
    repeat(25) {

   var newLat:Double = Random.nextDouble(currentPos.latitude-0.05, currentPos.latitude+0.05)
    var newLng:Double = Random.nextDouble(currentPos.longitude-0.05, currentPos.longitude+0.05)

    addCircle(map, newLat, newLng, 50 )
    createMarker(newLat, newLng, map, R.mipmap.ic_car_marker_foreground)

    }
}

fun addCircle(map:MapView, lat:Double, lng:Double, distance:Int) {
    val oPolygon = Polygon(map)
    val radius: Double = distance.toDouble()
    val circlePoints = arrayListOf<GeoPoint>()
    repeat(360) { f ->
        circlePoints.add(GeoPoint(lat, lng).destinationPoint(radius, f.toDouble()))
    }
    oPolygon.points = circlePoints
    oPolygon.fillPaint.color = Color.RED

    map.overlays.add(oPolygon)
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    var geoPoint by mutableStateOf(GeoPoint(49.12,12.1))
    var zoom = 100.0

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            // Creates the view
            MapView(context).apply {
                 this.minZoomLevel = 4.0
                // Do anything that needs to happen on the view init here
                // For example set the tile source or add a click listener
                setTileSource(TileSourceFactory.USGS_TOPO)
                setOnClickListener {
                    println("moin hier ist der fab.io_yt")
                }
                createMarker(49.15, 12.10, this, R.mipmap.ic_man_marker_foreground)
                addCircle(this, 49.15, 12.10, 1200)
                var currentPos:GeoPoint =GeoPoint(49.15, 12.10)
                generateRandomCars(this, currentPos)
                //scrollTo(49, 12)
                //zoomToBoundingBox(BoundingBox(geoPoint.latitude + zoom, geoPoint.longitude + zoom,
                  //  geoPoint.latitude - zoom, geoPoint.longitude - zoom), true)
                controller.setCenter(geoPoint)

                controller.animateTo(geoPoint, 13.50000000, 1000)
                //controller.zoomTo(10.0, 1000)

            }
        },
        update = { view ->
            // Code to update or recompose the view goes here
            // Since geoPoint is read here, the view will recompose whenever it is updated
            view.controller.setCenter(geoPoint)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WorldclickerTheme {
        Greeting("Android")
    }
}