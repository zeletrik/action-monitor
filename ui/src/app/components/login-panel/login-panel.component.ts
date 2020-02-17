import { Component, OnInit, Input, Output, EventEmitter} from '@angular/core';
import { HttpService } from '../../shared/service/HttpService';
import { FormControl, FormGroup, Validators } from '@angular/forms';

interface LoginRequst {
  username: string,
  password: string

}

@Component({
  selector: 'app-login-panel',
  templateUrl: './login-panel.html',
  styleUrls: ['./login-panel.scss']
})
export class LoginPanelComponent implements OnInit {

  @Output() authStatus = new EventEmitter<boolean>();

  loginGroup = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required])
  });

  loginFailed = false;

  ngOnInit() {}

  constructor(private httpService: HttpService) { }

  login() {
    this.loginFailed = false;
    const username = this.loginGroup.get('username').value;
    const password = this.loginGroup.get('password').value;
    const loginRequest = { username, password } as LoginRequst;

    this.httpService.post(
      'http://localhost:8080/user/login',
      loginRequest,
      true
    ).subscribe(
      result => { this.authStatus.emit(true);},
      error => { this.loginFailed = true; this.authStatus.emit(false);})
  }
}
