package de.fabioyt.worldclicker

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import de.fabioyt.worldclicker.ui.theme.WorldclickerTheme
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import kotlin.random.Random


class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    
    @SuppressLint("MissingPermission")
    fun obtieneLocalizacion(){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                println("FAB.IO_YT")
                latitude =  location?.latitude ?: 0.0
                longitude = location?.longitude ?: 0.0
                println("MOIN")
                println("lat: OIN$latitude long: $longitude")

                Toast.makeText(this,"$latitude and $longitude", Toast.LENGTH_LONG).show()

            }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        Configuration.getInstance().userAgentValue = applicationContext.packageName;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1);
        }

        super.onCreate(savedInstanceState)
       // Toast.makeText(this, "Moin", Toast.LENGTH_LONG).show()


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

    companion object {

    }

}

fun createMarker(lat:Double, lng:Double, map: MapView, icon:Int):Marker {
    val firstMarker = Marker(map)
    firstMarker.position = GeoPoint(lat, lng)
    firstMarker.icon = ContextCompat.getDrawable(map.context, icon)
//  firstMarker.title = "moin"
    map.overlays.add(firstMarker)
// "Invalidating" the map displays the marker as soon as it has been added.
    map.invalidate()

    return firstMarker
}
@RequiresApi(Build.VERSION_CODES.Q)
fun generateRandomCars(map: MapView, currentPos: GeoPoint) {
    repeat(25) {

    val newLat:Double = Random.nextDouble(currentPos.latitude-0.05, currentPos.latitude+0.05)
    val newLng:Double = Random.nextDouble(currentPos.longitude-0.05, currentPos.longitude+0.05)

    addCircle(map, newLat, newLng, 50 )
    createMarker(newLat, newLng, map, R.mipmap.ic_car_marker_foreground)

    }
}


@RequiresApi(Build.VERSION_CODES.Q)
fun addCircle(map:MapView, lat:Double, lng:Double, distance:Int) {
    val oPolygon = Polygon(map)
    val radius: Double = distance.toDouble()
    val circlePoints = arrayListOf<GeoPoint>()
    repeat(360) { f ->
        circlePoints.add(GeoPoint(lat, lng).destinationPoint(radius, f.toDouble()))
    }
    oPolygon.points = circlePoints
    oPolygon.fillPaint.color = Color(47, 128, 223, 100).toArgb()
    oPolygon.outlinePaint.color = Color(47, 128, 223, 255).toArgb()
    // println(oPolygon.outlinePaint.setColor(0xFFFFFF))

    map.overlays.add(oPolygon)
}

@RequiresApi(Build.VERSION_CODES.Q)
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
                setOnClickListener {
                    println("moin hier ist der fab.io_yt")
                }
                createMarker(49.15, 12.10, this, R.mipmap.ic_man_marker_foreground)
                var currentPos:GeoPoint =GeoPoint(49.15, 12.10)
                setBuiltInZoomControls(false);
                setMultiTouchControls(true);
                generateRandomCars(this, currentPos)
                //obtieneLocalizacion(this,R.mipmap.ic_man_marker_foreground )
                //scrollTo(49, 12)
                //zoomToBoundingBox(BoundingBox(geoPoint.latitude + zoom, geoPoint.longitude + zoom,
                  //  geoPoint.latitude - zoom, geoPoint.longitude - zoom), true)
                controller.setCenter(geoPoint)
                val mActivity = MainActivity()





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

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    WorldclickerTheme {
        Greeting("Android")
    }
}