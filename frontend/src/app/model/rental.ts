import { ImageUrls } from "./imageUrl";

export class Rental {
  id: number | undefined;
  
  beds: number | undefined;
  bathrooms: number | undefined;
  type: string | undefined;
  bedrooms: number | undefined;
  livingroom: boolean | undefined;
  area: number | undefined;

  description: string | undefined;

  smoking: boolean | undefined;
  pets: boolean | undefined;
  events: boolean | undefined;
  minDays: number | undefined;

  address: string | undefined;
  city: string | undefined;
  neighbor: string | undefined;
  transportation: string | undefined;
  coordinatex: number | undefined;
  coordinatey: number | undefined;

  host: number | undefined;

  availableDates: Date[] | undefined;
  rentalAvail: { date: string; available: boolean }[] = [];

  maxVisitors: number | undefined;
  minPrice: number | undefined;
  plusPricePerPerson: number | undefined;
  imageUrls: ImageUrls[] | undefined;
  listUrls: string | undefined;

  wifi: boolean | undefined;
  ac: boolean | undefined;
  kitchen: boolean | undefined;
  tv: boolean | undefined;
  parking: boolean | undefined;
  elevator: boolean | undefined;

  reviewSum: number | undefined;
  reviewAvg: number | undefined;

}


