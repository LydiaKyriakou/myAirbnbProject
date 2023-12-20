import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/services/user.service';
import { AuthService } from 'src/app/auth/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent  implements OnInit {
  user: User | undefined;
  password: string = '';
  confirmPassword: string = '';
  newUser: User = new User(); 
  decodedToken: any;
  name: string[] = [];
  urls: string[] = [];

  isPasswordVisible = false;
  hiddenEyeIconUrl = '../../../assets/images/hidden-password.png';
  visibleEyeIconUrl = '../../../assets/images/view-password.png';

  togglePasswordVisibility() {
    this.isPasswordVisible = !this.isPasswordVisible;
  }

  constructor(private userService: UserService, 
              private authService: AuthService,
              private router: Router
  ) {}


  onSelect(event: any) {
    if (event.target.files) {
      for (let i = 0; i < event.target.files.length; i++) {
        var reader = new FileReader();
        this.name.push(event.target.files[i].name);
  
        reader.readAsDataURL(event.target.files[i]);
        reader.onload = (events: any) => {
          this.urls.push(events.target.result);

          if (this.user) {
            this.user.imageUrl = this.urls.join(',');
          }
        }
      }
    }
  }


  updateUsername() {
    if (this.user && this.user.id) { 

      if (this.password != this.confirmPassword) {
        alert('Password and Confirm Password should be the same!');
      }
      else if (this.password == '' && this.confirmPassword == '') {
        const user = {
          username: this.user.username,
          firstName: this.user.firstName,
          lastName: this.user.lastName,
          email: this.user.email,
          phone: this.user.phone,
          imageUrl: this.user.imageUrl,
        }

        this.userService.updateUser(this.user.id, user).subscribe(
          (response) => {
            // Handle the response, e.g., show a success message
            console.log('User updated successfully:', response);
            alert('User updated successfully');
            this.router.navigate(['/profile'])
          },
          (error) => {
            // Handle the error, e.g., display an error message
            console.error('Error updating user:', error);
            alert('Username is already taken ' + error.message);
          }
        );
      }
      else {
        this.user.password = this.password;

        this.userService.updateUser(this.user.id, this.user).subscribe(
            (response) => {
              // Handle the response, e.g., show a success message
              console.log('User updated successfully:', response);
              alert('User updated successfully');
              this.router.navigate(['/profile'])
            },
            (error) => {
              // Handle the error, e.g., display an error message
              console.error('Error updating user:', error);
              alert('Error updating user: ' + error.message);
            }
        );
      }

    }
  }


  ngOnInit() {
    this.decodedToken = this.authService.getDecodedAccessToken();

    if (this.decodedToken && this.decodedToken.userId) {
      this.userService.getUserById(this.decodedToken.userId).subscribe(user => {
        this.user = user;
      });
    }
  }

}
