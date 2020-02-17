import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { HttpService } from '../../shared/service/HttpService';
import { Message } from '../../shared/domain/Message';
import { User } from '../../shared/domain/User';

import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';

import { environment } from 'src/environments/environment';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  baseUrl = '';
  webSocketEndPoint = '';
  room = "/chat/secured/room";
  specificUser = "/secured/user/queue/specific-user";
  stompClient: any;
  sessionId = '';

  serverStatus = 'Down';
  serverVersion = '';

  messages: Message[] = []

  sendGroup = new FormGroup({
    to: new FormControl('', [Validators.required]),
    text: new FormControl('', [Validators.required])
  });

    loggedIn = false;
    user: User;
    users: User[]

    constructor(private httpService: HttpService) {
      this.baseUrl = environment.backendEndpoint;
      this.webSocketEndPoint = 'http://' + this.baseUrl + '/socket';
    }

    ngOnInit() {
      this.init();
      this.getServerData();
    }

    getServerData() {
      this.httpService
      .get('http://' + this.baseUrl + '/info/health')
      .subscribe(resp => this.serverStatus  = resp.status);

      this.httpService
      .get('http://' + this.baseUrl + '/info/version')
      .subscribe(resp => this.serverVersion  = resp.version)
    }

    sendMessage(to: string) {
      const msg = new Message();
      msg.from = this.user.username;
      msg.to = to;
      msg.text = this.sendGroup.get('text').value;
      this.send(msg);
    }

    authStateChanger(state: boolean) {
      console.log('Auth state changed to: ' + state)
      this.loggedIn = state;
      if (this.loggedIn) {
        this.init();
      }
    }

    logout() {
      this.httpService
      .get('http://' + this.baseUrl + '/user/logout')
      .subscribe()
       this.loggedIn = false
    }

    currentUser() {
      this.httpService
      .get('http://' + this.baseUrl + '/user/current')
      .subscribe(resp => {
        this.user = resp;
        this.loggedIn = true;
      },
        () => this.loggedIn = false)
    }

    retrieveOtherUsers() {
      this.httpService.get('http://' + this.baseUrl + '/user/others')
      .subscribe( resp => this.users = resp,
                  () => this.loggedIn = false)
    }

    init() {
      this.currentUser();
      this.retrieveOtherUsers();
      this.connect();
    }

    connect() {
      console.log('Initialize WebSocket Connection');
      const ws = new SockJS(this.webSocketEndPoint);
      this.stompClient = Stomp.over(ws);
      let that = this;
      this.stompClient.connect({}, function(frame) {
        var url = that.stompClient.ws._transport.url;
        url = url.replace('ws://'+ that.baseUrl + '/socket',  '');
        url = url.replace('/websocket', '');
        url = url.replace(/^\/[0-9]+\//, '');
        console.log('Your current session is: ' + url);
        that.sessionId = url;

        const subscribeUrl =  that.specificUser + '-user' + that.sessionId;

        that.stompClient.subscribe(subscribeUrl, (message) => {
          console.log(message);
          if(message.body) {
          
            const body = JSON.parse(message.body) as Message;

            that.messages.push(body)
            console.log('Message: ' + message.body);
          }
        });
      }, this.errorCallBack);
    }

  send(message: Message) {
    this.messages.push(message)
    this.stompClient.send(this.room, {}, JSON.stringify(message));
  }

  errorCallBack(error) {
    console.log('errorCallBack -> ' + error)
    setTimeout(() => {
        this.connect();
    }, 5000);
  }
}
