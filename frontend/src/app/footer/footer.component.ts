import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-footer',
  templateUrl: './footer.component.html',
  styleUrls: ['./footer.component.css']
})
export class FooterComponent implements OnInit {
  decodedToken: any;

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.decodedToken = this.authService.getDecodedAccessToken();
  }
}
