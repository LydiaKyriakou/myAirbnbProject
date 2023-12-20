import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../auth/auth.service';
import { UserService } from '../services/user.service';
import { User } from '../model/user';

@Component({
  selector: 'app-log-in',
  templateUrl: './log-in.component.html',
  styleUrls: ['./log-in.component.css']
})
export class LogInComponent {
  isPasswordVisible = false;
  hiddenEyeIconUrl = '../../assets/images/hidden-password.png';
  visibleEyeIconUrl = '../../assets/images/view-password.png';

  user: User | undefined;

  form = new FormGroup({
    username: new FormControl(null, Validators.required),
    password: new FormControl(null, Validators.required),
  });

  constructor(private authService: AuthService, 
              private router: Router, 
              private userService: UserService
  ) {}

  togglePasswordVisibility() {
    this.isPasswordVisible = !this.isPasswordVisible;
  }

  submitForm() {
    const username = this.form.get('username')?.value;
    const password = this.form.get('password')?.value;
  
    if (!username || !password) {
      alert('Please fill in all the required fields.');
      return;
    }
  

    this.authService.login(username, password).subscribe(
      (user) => {
        const decodedToken = this.authService.getDecodedAccessToken();
        const roles = user.roles;

        if (roles.includes('ROLE_ADMIN')) {      
          this.router.navigate(['/admin']);
        } else if (roles.includes('ROLE_USER')) {
          this.router.navigate(['']);
        } else if (roles.includes('ROLE_HOST')) {

          this.userService.getUserById(decodedToken.userId).subscribe(user => {
            this.user = user;
          
            if (this.user.approval == true) {
              this.router.navigate(['/host']);
            } else {
              alert('Your request is being processed by the administrator. You have to wait for approval to log in.');
              this.authService.logout();
              this.user = undefined;
            }
          });
        }
         
      },
      (error) => {
        console.error('Error login user:', error);
        alert(`An error occurred while login. Please try again later.`);
      }
    );

  }

}



