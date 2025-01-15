package ro.pub.cs.systems.eim.testpracticaltest02v1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val ghelmegioaia = LatLng(44.5, 25.5)
        googleMap.addMarker(MarkerOptions().position(ghelmegioaia).title("Ghelmegioaia, Romania"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ghelmegioaia, 15f))
    }
}