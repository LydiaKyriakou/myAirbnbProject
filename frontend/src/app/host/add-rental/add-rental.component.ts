import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Rental } from 'src/app/model/rental';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/auth/auth.service';
import * as L from 'leaflet';
import 'leaflet-control-geocoder';
import { ImageUrls } from 'src/app/model/imageUrl';

@Component({
  selector: 'app-add-rental',
  templateUrl: './add-rental.component.html',
  styleUrls: ['./add-rental.component.css']
})
export class AddRentalComponent implements OnInit {
  rental: Rental = new Rental();
  decodedToken: any;

  selectedStartDate: Date = new Date();
  selectedFinishDate: Date = new Date();

  name: string[] = [];
  urls: string[] = [];
  listUrl: ImageUrls[] | undefined;

  availdate: string = '';
  datesArray: Date[] = [];
  rentalAvail: { date: string; available: boolean }[] = [];

  coordinateX: number | undefined;
  coordinateY: number | undefined;
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

  constructor(private authService: AuthService, private http: HttpClient, private router: Router) {}

  onMapReady($event: L.Map) {
    this.map = $event;
    var geocoder = (L.Control as any).geocoder({
      defaultMarkGeocode: true
    })
      .on('markgeocode', (e: { geocode: { center: any } }) => {
        var marker = e.geocode.center;
        this.coordinateX = marker.lat;
        this.coordinateY = marker.lng;
        this.NoneCoordinate = false;
  
        const customIcon = L.icon({
          iconUrl: '../../../assets/images/placeholder.png', 
          iconSize: [32, 32], 
          iconAnchor: [16, 32], 
          popupAnchor: [0, -32]
        });

        
        if (this.coordinateX !== undefined && this.coordinateY !== undefined) {
          L.marker([this.coordinateX, this.coordinateY], { icon: customIcon }).addTo(this.map);
        }
      })
      .addTo(this.map);
  }


  onSelect(event: any) {
    if (event.target.files) {
      for (let i = 0; i < event.target.files.length; i++) {
        const reader = new FileReader();
        this.name.push(event.target.files[i].name);
  
        reader.readAsDataURL(event.target.files[i]);
  
        reader.onload = (events: any) => {
          const imageUrlObject: ImageUrls = { imageUrl: events.target.result };
          this.rental.imageUrls = this.rental.imageUrls || []; // Initialize if undefined
          this.rental.imageUrls.push(imageUrlObject);
        };
      }
    }
  }



  localImageUrl: string | undefined;

  onFileSelected(event: any) {
    const fileInput = event.target;
    if (fileInput.files.length > 0) {
      const selectedFile = fileInput.files[0];
      this.readAndDisplayFile(selectedFile);
    }
  }

  readAndDisplayFile(file: File) {
    const reader = new FileReader();

    reader.onload = (e) => {
      // This callback function will be called when the file reading is complete
      const result = reader.result as string;
      this.localImageUrl = result;

      // Assuming imageUrls is an array, push the result to it
      if (!this.rental.imageUrls) {
        this.rental.imageUrls = [];
      }
      const imageUrlObject: ImageUrls = { imageUrl: result };
      this.rental.imageUrls.push(imageUrlObject);
    };

    // Read the file as a data URL (base64)
    reader.readAsDataURL(file);
  }


  saveDate() {
    const selectedDate = new Date(this.availdate);

    if (!this.dateExists(selectedDate)) {
      this.datesArray.push(selectedDate);
  
      // Convert the JavaScript Date to a string in the format "YYYY-MM-DD"
      const dateString = selectedDate.toISOString().slice(0, 10);
  
      // Push the date into the rentalAvail array with 'available' set to true
      this.rentalAvail.push({ date: dateString, available: true });
  
      // Clear the input field
      this.availdate = '';
    } else {
      // Handle the case where the date already exists
      alert('This date already exists in the list.');
    }
  }

  dateExists(date: Date): boolean {
    return this.datesArray.some(existingDate => {
      // Compare dates by converting them to ISO date strings
      return existingDate.toISOString().slice(0, 10) === date.toISOString().slice(0, 10);
    });
  }

  deleteDate(date: Date): void {
    const dateIndex = this.datesArray.findIndex(existingDate => {
      return existingDate.toISOString().slice(0, 10) === date.toISOString().slice(0, 10);
    });
  
    if (dateIndex !== -1) {
      // Remove the date from the datesArray and rentalAvail array
      this.datesArray.splice(dateIndex, 1);
      this.rentalAvail.splice(dateIndex, 1);
    }
  }
  

  save() {
    // if (!this.isFormValid()) {
    //   alert('Please fill in all the required fields.');
    //   return;
    // }

    // if (!this.isDataValid()) {
    //   alert('Data is not valid.');
    //   return;
    // }

    // Send the user data to the server using HTTP POST request
    this.decodedToken = this.authService.getDecodedAccessToken();
    const username = this.decodedToken.username;

    const apiUrl = `https://localhost:8443/user/${username}/add-rental`;
    

    this.rental.coordinatex = this.coordinateX;
    this.rental.coordinatey = this.coordinateY;

    this.rental.rentalAvail = this.rentalAvail;

    this.http.post<Rental>(apiUrl, this.rental)
      .subscribe(
        (resultData: any) => {
          console.log(resultData);
          alert('Rental Added Successfully');
          this.router.navigate(['/host']);
        },
        (error) => {
          console.error('Error adding rental:', error);
          alert('An error occurred while adding rental. Please try again later.');
        }
      );
  }

  ngOnInit() {
    this.decodedToken = this.authService.getDecodedAccessToken();

    if (!this.decodedToken) {
      this.router.navigate(['/access-denied']);
    } else if (this.decodedToken.roles && !this.decodedToken.roles.includes('ROLE_HOST')) {
      this.router.navigate(['/access-denied']);
    }
  }

  isFormValid(): boolean {
    return !!(
      // this.rental.coordinatex &&
      // this.rental.coordinatey &&
      // this.rental.address &&
      // this.rental.city &&
      // this.rental.neighbor &&
      // this.rental.transportation &&

      // this.rental.availableDates &&

      this.rental.maxVisitors &&
      // this.rental.minPrice &&
      // this.rental.plusPricePerPerson &&
      // this.rental.type &&

      // this.rental.minDays &&
      // this.rental.description &&

      // this.rental.beds &&
      // this.rental.bathrooms &&
      // this.rental.bedrooms &&
      this.rental.area 
    
    );
  }

  isDataValid(): boolean {
    return !!(
      Number.isInteger(this.rental.maxVisitors) &&
      Number.isInteger(this.rental.beds) &&
      Number.isInteger(this.rental.bedrooms) &&
      Number.isInteger(this.rental.area) &&
      Number.isInteger(this.rental.minDays)     
    );
  }

}
