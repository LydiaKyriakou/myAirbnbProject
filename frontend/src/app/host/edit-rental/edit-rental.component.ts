import { Component, OnInit } from '@angular/core';
import { Rental } from 'src/app/model/rental';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/auth/auth.service';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { RentaldService } from 'src/app/services/rental.service';
import { ImageUrls } from 'src/app/model/imageUrl';
import * as L from 'leaflet';
import 'leaflet-control-geocoder';

@Component({
  selector: 'app-edit-rental',
  templateUrl: './edit-rental.component.html',
  styleUrls: ['./edit-rental.component.css']
})
export class EditRentalComponent implements OnInit {
  oldRental: Rental | undefined;
  newRental: any;

  decodedToken: any;

  rentalId: number | undefined;

  availableDates: { date: string; available: boolean }[] = [];
  newRentalAvail: { date: string; available: boolean }[] = [];

  name: string[] = [];
  urls: string[] = [];
  listUrl: ImageUrls[] = [];

  selectedStartDate: Date = new Date();
  selectedFinishDate: Date = new Date();

  availdate: string = '';
  datesArray: Date[] = [];
  rentalAvail: { date: string; available: boolean }[] = [];

  oldDate: any;

  coordinateX: number | undefined;
  coordinateY: number | undefined;
  x: number | null = null;
  y: number | null = null;
  NoneCoordinate: boolean = false;

  map!: L.Map;
  markers: L.Marker[] = [];
  secondMap!: L.Map; 
  options = {
    layers: [
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
      })
    ],
    zoom: 10,
    center: { lat: 37.95591229014076, lng: 23.751258552074436 }
  }

  constructor( private authService: AuthService, 
              private http: HttpClient, 
              private router: Router,
              private route: ActivatedRoute,
              private rentalService: RentaldService
  ) {}


  onMapReady($event: L.Map) {
    this.map = $event;
    
    if (this.oldRental?.coordinatex && this.oldRental?.coordinatey) {
      const customIcon = L.icon({
        iconUrl: '../../../assets/images/placeholder.png',
        iconSize: [32, 32],
        iconAnchor: [16, 32],
        popupAnchor: [0, -32]
      });
  
      const marker = L.marker(
        [this.oldRental.coordinatex, this.oldRental.coordinatey],
        { icon: customIcon }
      ).addTo(this.map);
      
      // You can also add a popup to the marker if needed
      marker.bindPopup(
        `Coordinates: ${this.oldRental.coordinatex}, ${this.oldRental.coordinatey}`
      ).openPopup();
    }
  
    var geocoder = (L.Control as any).geocoder({
      defaultMarkGeocode: true
    })
      .on('markgeocode', (e: { geocode: { center: any } }) => {
        var marker = e.geocode.center;
        this.coordinateX = marker.lat;
        this.coordinateY = marker.lng;
        this.NoneCoordinate = false;
      })
      .addTo(this.map);
  }

  saveDate() {
    const selectedDate = new Date(this.availdate);
    
    if (!this.dateExists(selectedDate)) {
      this.datesArray.push(selectedDate);
      
      // Convert the JavaScript Date to a string in the format "YYYY-MM-DD"
      const dateString = selectedDate.toISOString().slice(0, 10);
      
      // Push the date into the rentalAvail array with 'available' set to true
      this.rentalAvail.push({ date: dateString, available: true });
      this.availableDates.push({ date: dateString, available: true });
      this.newRentalAvail.push({ date: dateString, available: true });
  
      // Clear the input field
      this.availdate = '';
    } else {
      // Handle the case where the date already exists
      alert('This date already exists in the list.');
    }
  }

  dateExists(date: Date): boolean {
    return this.availableDates.some(existingDate => {
      // Convert the existingDate.date string to a Date object
      const existingDateObject = new Date(existingDate.date);
      
      // Compare dates by converting them to ISO date strings
      return existingDateObject.toISOString().slice(0, 10) === date.toISOString().slice(0, 10);
    });
  }

  onSelect(event: any) {
    if (event.target.files) {
      for (let i = 0; i < event.target.files.length; i++) {
        const reader = new FileReader();
        this.name.push(event.target.files[i].name);
  
        reader.readAsDataURL(event.target.files[i]);
  
        reader.onload = (events: any) => {
          const imageUrlObject: ImageUrls = { imageUrl: events.target.result };
          this.listUrl.push(imageUrlObject);
        };
      }
    }
  }

  deleteImage(image: any) {
    if (image.id) {
      this.rentalService.deleteImage(image.id).subscribe(
        () => {
          // Image successfully deleted, you can update your UI here if needed
          console.log('Image deleted successfully.');
          window.location.reload();
        },
        (error) => {
          // Handle error, show error message, etc.
          console.error('Error deleting image:', error);
        }
      );
    }
  }

  updateRental() {
    if (this.coordinateX && this.coordinateY) {
      this.x = this.coordinateX;
      this.y = this.coordinateY;
    }

    if (this.oldRental && this.oldRental.id) {
      this.newRental = {
        coordinatex: this.x,
        coordinatey: this.y,
        address: this.oldRental.address,
        city:this.oldRental.city,
        neighbor: this.oldRental.neighbor,
        transportation:this.oldRental.transportation,
        rentalAvail: this.newRentalAvail,
        maxVisitors: this.oldRental.maxVisitors,
        minPrice: this.oldRental.minPrice,
        plusPricePerPerson: this.oldRental.plusPricePerPerson,
        type: this.oldRental.type,
        smoking: this.oldRental.smoking,
        pets: this.oldRental.pets,
        events: this.oldRental.events,
        minDays: this.oldRental.minDays,
        description: this.oldRental.description,
        beds: this.oldRental.beds,
        bathrooms: this.oldRental.bathrooms,
        bedrooms: this.oldRental.bedrooms,
        area: this.oldRental.area,
        livingroom: this.oldRental.livingroom,
        wifi: this.oldRental.wifi,
        ac: this.oldRental.ac,
        kitchen: this.oldRental.kitchen,
        tv: this.oldRental.tv,
        parking: this.oldRental.parking,
        elevator: this.oldRental.elevator,
        imageUrls:  this.listUrl
      }
  
      this.rentalService.updateRental(this.oldRental.id, this.newRental).subscribe(
        (response) => {
          // Handle the response, e.g., show a success message
          console.log('Rental updated successfully:', response);
          alert('Rental updated successfully.');
          this.router.navigate(['/host']);
        },
        (error) => {
          // Handle the error, e.g., display an error message
          console.error('Error updating rental:', error);
          alert('Error updating rental.');
        }
      );
    }
  }


  removeDate(date: any) {
    if (this.oldRental?.rentalAvail) {
      console.log(this.oldRental?.rentalAvail)
    }

    this.rentalService.deleteRentalAvailability(date.id).subscribe(
      (response) => {
        // Handle the response, e.g., show a success message
        console.log('Rental availability deleted successfully:', response);
        this.oldRental?.rentalAvail
      },
      (error) => {
        // Handle the error, e.g., display an error message
        console.error('Error deleting rental availability:', error);
      }
    );
  }

  refreshRentalAvail() {
    if (this.rentalId && this.oldRental) {
      this.rentalService.getById(this.rentalId).subscribe(
        (rental: Rental) => {
          if (rental && rental.rentalAvail && this.oldRental) {
            this.oldRental.rentalAvail = rental.rentalAvail;
          }
        },
        (error) => {
          console.error('Error fetching rental:', error);
        }
      );
    } else {
      console.error('rentalId or oldRental is undefined.');
    }
  }


  ngOnInit() {
    this.decodedToken = this.authService.getDecodedAccessToken();
    
    if (!this.decodedToken) {
      this.router.navigate(['/access-denied']);
    } else if (this.decodedToken.roles && !this.decodedToken.roles.includes('ROLE_HOST')) {
      this.router.navigate(['/access-denied']);
    }

    this.rentalId = Number(this.route.snapshot.paramMap.get('id'));

    this.rentalService.getById(this.rentalId).subscribe(
      (rental: Rental) => {
        this.oldRental = rental;

        if (this.oldRental?.rentalAvail) {
          this.oldDate = this.oldRental.rentalAvail;
        }

        this.availableDates = rental.rentalAvail
          .filter((avail: any) => avail.available === true)
          .map((avail: any) => ({
            date: avail.date,
            available: avail.available
          }));
        
      },
      (error) => {
        console.error('Error fetching rental:', error);
      }
    );
  }

}
