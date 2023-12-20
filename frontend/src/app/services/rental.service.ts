import { Injectable } from '@angular/core';
import { Rental } from '../model/rental';
import { Review } from '../model/review';
import { Observable } from "rxjs";
import { HttpClient, HttpHeaders } from "@angular/common/http";

const httpOptions = {
  headers: new HttpHeaders({'Accept': 'application/json', 'Content-Type': 'application/json'})
};

@Injectable({
  providedIn: 'root'
})
export class RentaldService { 
  private baseUrl = 'https://localhost:8443';
  private rentalsUrl ='https://localhost:8443/rental';
  private resultsUrl ='https://localhost:8443/results';
  private reviewsUrl ='https://localhost:8443/reviews';
  private reservationsUrl ='https://localhost:8443/reservations';
  
  constructor(private http: HttpClient) { }
  
  getRentals(): Observable<Rental[]> {
    const apiUrl = `${this.rentalsUrl}/all`;  
    return this.http.get<Rental[]>(apiUrl);
  }

  updateRental(id: number, updatedRental: Rental): Observable<Rental> {
    const url = `${this.rentalsUrl}/update/${id}`;
    return this.http.put<Rental>(url, updatedRental);
  }

  getRentalsByHost(hostlId: number): Observable<Rental[]> {
    const url = `${this.rentalsUrl}/host/${hostlId}`;
    return this.http.get<Rental[]>(url);
  }

  getById(rentalId: number): Observable<Rental> {
    const url = `${this.rentalsUrl}/find/${rentalId}`;
    return this.http.get<Rental>(url);
  }

  deleteRental(rental: Rental) {
    const rentalId = rental.id; 
    const apiUrl = `${this.rentalsUrl}/delete/${rentalId}`;
  
    this.http.delete(apiUrl).subscribe(
      () => {
        console.log('Rental Deleted');
      },
      (error) => {
        console.error('Error deleting rental:', error);
      }
    );
  }


  getSomeRentals(city: string, checkin: Date, checkout: Date, guests: number, page: number, size: number): Observable<any> {
    const apiUrl = `${this.resultsUrl}/find/checkin/${checkin}/checkout/${checkout}/city=${city}&guests=${guests}&page=${page}&size=${size}`;  
    return this.http.get<any>(apiUrl);
  }

  getSomeRentalsFilters(page: number, size: number): Observable<any> {
    const apiUrl = `${this.resultsUrl}/sendResults&page=${page}&size=${size}`;  
    return this.http.get<any>(apiUrl);
  }

  getRecommendationsRentals(guestId: number, page: number, size: number): Observable<any> {
    const apiUrl = `${this.resultsUrl}/sendRecommendations&guestId=${guestId}&page=${page}&size=${size}`;  
    return this.http.get<any>(apiUrl);
  }

  postFilters(filters: any): Observable<any> {
    const url = `${this.resultsUrl}/post-filters`; 
    return this.http.post<Review>(url, filters);
  }


  getReviews(): Observable<any> {
    const apiUrl = `${this.reviewsUrl}/all`;
    return this.http.get<any>(apiUrl);
  }

  getAllReviews(rentalId: number): Observable<any> {
    const apiUrl = `${this.reviewsUrl}/rental/${rentalId}`;
    return this.http.get<any>(apiUrl);
  }

  addReviewToRental(review: Review): Observable<Review> {
    const url = `${this.reviewsUrl}/${review.rental_id}/add`; 
    return this.http.post<Review>(url, review);
  }

  getReviewsSum(rentalId: number): Observable<number> {
    const apiUrl = `${this.reviewsUrl}/reviewsSum/rental=${rentalId}`;
    return this.http.get<number>(apiUrl);
  }

  getReviewsAvg(rentalId: number): Observable<number> {
    const apiUrl = `${this.reviewsUrl}/reviewsAvg/rental=${rentalId}`;
    return this.http.get<number>(apiUrl);
  }


  makeReservation(reservation: any): Observable<any> {
    const url = `${this.reservationsUrl}/add`; 
    return this.http.post<Review>(url, reservation);
  }

  deleteImage(imageId: number): Observable<any> {
    const url = `${this.baseUrl}/images/delete/${imageId}`;
    return this.http.delete(url);
  }

  deleteRentalAvailability(id: number): Observable<any> {
    const url = `${this.baseUrl}/rentaldates/delete/${id}`;
    return this.http.delete(url);
  }
}
