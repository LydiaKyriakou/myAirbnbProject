import { Component, OnInit } from '@angular/core';
import { saveAs } from "file-saver";
import { UserService } from 'src/app/services/user.service';
import { RentaldService } from 'src/app/services/rental.service';
import { AuthService } from 'src/app/auth/auth.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-export-data',
  templateUrl: './export-data.component.html',
  styleUrls: ['./export-data.component.css']
})
export class ExportDataComponent implements OnInit {
  data: any = [];

  decodedToken: any;

  constructor ( private userService: UserService,
                private rentalService: RentaldService,
                private authService: AuthService,
                private router: Router

  ) {}


  ngOnInit() {
    this.decodedToken = this.authService.getDecodedAccessToken();
  
    if (!this.decodedToken) {
      this.router.navigate(['/access-denied']);
    } else if (this.decodedToken.roles && !this.decodedToken.roles.includes('ROLE_ADMIN')) {
      this.router.navigate(['/access-denied']);
    } 
  }


  exportTojson(data: string) {
    if (data === 'users' ) {
      this.userService.getUsers().subscribe(response => {
        this.data = response;
  
        console.log("data", this.data)
        let exportData = this.data;
        return saveAs(
           new Blob([JSON.stringify(exportData, null, 2)], { type: 'JSON' }), 'users.json'
        );
      });
    }

    if (data === 'rentals' ) {
      this.rentalService.getRentals().subscribe(response => {
        this.data = response;
  
        console.log("data", this.data)
        let exportData = this.data;
        return saveAs(
          new Blob([JSON.stringify(exportData, null, 2)], { type: 'JSON' }), 'rental.json'
        );
      });
    }

    if (data === 'reservations' ) {
      this.userService.getReservations().subscribe(response => {
        this.data = response;
  
        console.log("data", this.data)
        let exportData = this.data;
        return saveAs(
          new Blob([JSON.stringify(exportData, null, 2)], { type: 'JSON' }), 'reservations.json'
        );
      });
    }

    if (data === 'reviews' ) {
      this.rentalService.getReviews().subscribe(response => {
        this.data = response;
  
        console.log("data", this.data)
        let exportData = this.data;
        return saveAs(
          new Blob([JSON.stringify(exportData, null, 2)], { type: 'JSON' }), 'reviews.json'
        );
      });
    }
  }


  exportToXml(data: string) {
    if (data === 'users' ) {
      this.userService.getUsers().subscribe(response => {
        this.data = response;
        console.log("data", this.data)
  
        const xmlData = this.createXmlUsers(this.data);
        saveAs(new Blob([xmlData], { type: 'application/xml' }), 'users.xml');
      });
    }

    if (data === 'rentals' ) {
      this.rentalService.getRentals().subscribe(response => {
        this.data = response;
        console.log("data", this.data)
  
        const xmlData = this.createXmlRentals(this.data);
        saveAs(new Blob([xmlData], { type: 'application/xml' }), 'rentals.xml');
      });
    }

    if (data === 'reservations' ) {
      this.userService.getReservations().subscribe(response => {
        this.data = response;
        console.log("data", this.data)
  
        const xmlData = this.createXmlReservations(this.data);
        saveAs(new Blob([xmlData], { type: 'application/xml' }), 'reservations.xml');
      });
    }

    if (data === 'reviews' ) {
      this.rentalService.getReviews().subscribe(response => {
        this.data = response;
        console.log("data", this.data)
  
        const xmlData = this.createXmlReviews(this.data);
        saveAs(new Blob([xmlData], { type: 'application/xml' }), 'reviews.xml');
      });
    }
  }


  private createXmlUsers(users: any[]): string {
    let xml = '<?xml version="1.0" encoding="UTF-8" ?>\n';
    xml += '<users>\n';
  
    for (const user of users) {
      xml += '  <user>\n';
  
      for (const property in user) {
        if (user.hasOwnProperty(property)) {
          xml += `    <${property}>${user[property]}</${property}>\n`;
        }
      }
  
      xml += '  </user>\n';
    }
  
    xml += '</users>';
    return xml;
  }

  private createXmlRentals(rentals: any[]): string {
    let xml = '<?xml version="1.0" encoding="UTF-8" ?>\n';
    xml += '<rentals>\n';
  
    for (const rental of rentals) {
      xml += '  <rental>\n';
  
      for (const property in rental) {
        if (rental.hasOwnProperty(property)) {
          xml += `    <${property}>${rental[property]}</${property}>\n`;
        }
      }
  
      xml += '  </rental>\n';
    }
  
    xml += '</rentals>';
    return xml;
  }

  private createXmlReservations(reservations: any[]): string {
    let xml = '<?xml version="1.0" encoding="UTF-8" ?>\n';
    xml += '<reservations>\n';
  
    for (const reservation of reservations) {
      xml += '  <reservation>\n';
  
      for (const property in reservation) {
        if (reservation.hasOwnProperty(property)) {
          xml += `    <${property}>${reservation[property]}</${property}>\n`;
        }
      }
  
      xml += '  </reservation>\n';
    }
  
    xml += '</reservations>';
    return xml;
  }

  private createXmlReviews(reviews: any[]): string {
    let xml = '<?xml version="1.0" encoding="UTF-8" ?>\n';
    xml += '<reviews>\n';
  
    for (const review of reviews) {
      xml += '  <review>\n';
  
      for (const property in review) {
        if (review.hasOwnProperty(property)) {
          xml += `    <${property}>${review[property]}</${property}>\n`;
        }
      }
  
      xml += '  </review>\n';
    }
  
    xml += '</reviews>';
    return xml;
  }
}
