import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../model/user';
import { Role } from '../model/role';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  user: User = new User(); 

  name: string[] = [];
  urls: string[] = [];

  confirmPassword: string | undefined;
  userType: string | undefined;

  isPasswordVisible = false;
  hiddenEyeIconUrl = '../../assets/images/hidden-password.png';
  visibleEyeIconUrl = '../../assets/images/view-password.png';
  
  
  constructor(private http: HttpClient, 
              private router: Router
  ) { }


  onSelect(event: any) {
    if (event.target.files) {
      for (let i = 0; i < event.target.files.length; i++) {
        var reader = new FileReader();
        this.name.push(event.target.files[i].name);
  
        reader.readAsDataURL(event.target.files[i]);
        reader.onload = (events: any) => {
          this.urls.push(events.target.result);

          this.user.imageUrl = this.urls.join(',');
        }
      }
    }
  }

  
  save() {
    if (!this.isFormValid()) {
      alert('Please fill in all the required fields.');
      return;
    }

    if (!(this.user.password == this.confirmPassword)) {
      alert('Password and Confirm Password is not the same.');
      return;
    }

    if (this.userType == 'User') {
      this.user.roles = [
        { name: Role.ROLE_USER }
      ];
    } else if (this.userType == 'Host') {
      this.user.roles = [
        { name: Role.ROLE_HOST }
      ];
    }

    console.log(" this.user", this.user)
    this.http.post<User>('https://localhost:8443/user/add', this.user)
      .subscribe(
        (resultData: any) => {
          console.log(resultData);
          if (this.userType == 'Host') {
            alert('You are registered as host. You must wait for your approval from the administrator before log in');
          }
          else if (this.userType == 'User'){
            alert('User Registered Successfully. You can now log in.');
          }

          this.router.navigate(['']);
        },
        (error) => {
          console.error('Error registering user:', error);
          alert('This username or email is already taken.');
        }
    );

  }

  // Function to check if the form is valid (all required fields are filled)
  isFormValid(): boolean {
    return !!(
      this.user.username &&
      this.user.firstName &&
      this.user.lastName &&
      this.user.email &&
      this.user.phone &&
      this.userType &&
      this.user.password  &&
      this.confirmPassword 
      // this.user.imageUrl
    );
  }

  togglePasswordVisibility() {
    this.isPasswordVisible = !this.isPasswordVisible;
  }

}

