/**
 * Created by sergio on 14/07/14.
 */
var geocoder = new google.maps.Geocoder();

function geocodePosition(pos) {
    geocoder.geocode({
        latLng: pos
    }, function(responses) {
        if (responses && responses.length > 0) {
            updateMarkerAddress(responses[0].formatted_address);
        } else {
            updateMarkerAddress('Cannot determine address at this location.');
        }
    });
}

function updateMarkerPosition(latLng) {
    document.getElementById('info').innerHTML = [
        latLng.lat(),
        latLng.lng()
    ].join(', ');
    document.getElementById('latitud' ).value = latLng.lat();
    document.getElementById('longitud' ).value = latLng.lng();
}

function updateMarkerAddress(str) {
    document.getElementById('address').innerHTML = str;
    document.getElementById('direccion').value = str;
}

function map_input(map, lat, lan) {
    var latLng = new google.maps.LatLng(lat, lan);

    var marker = new google.maps.Marker({
        position: latLng,
        title: 'Point A',
        map: map,
        draggable: true
    });

    // Update current position info.
    updateMarkerPosition(latLng);
    geocodePosition(latLng);

    // Add dragging event listeners.
    google.maps.event.addListener(marker, 'dragstart', function() {
        updateMarkerAddress('Dragging...');
    });

    google.maps.event.addListener(marker, 'drag', function() {
        updateMarkerPosition(marker.getPosition());
    });

    google.maps.event.addListener(marker, 'dragend', function() {
        geocodePosition(marker.getPosition());
    });
}