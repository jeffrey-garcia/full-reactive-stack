import { Injectable, NgZone } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import * as EventSource from 'eventsource';

import { Customer } from './model/Customer';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class RestService {
  private getCustomerUrl = 'http://localhost:8081/customer';  
  private getReactiveCustomerUrl = 'http://localhost:8081/rx/customers';
  //private getReactiveCustomerUrl = 'https://localhost:8081/services/apexrest/rx/customers';

  constructor(
    private zone: NgZone,
    private http: HttpClient
  ) { }

  getBlockingCustomer(): Observable<Customer> {
    return this.http.get<Customer>(this.getCustomerUrl)
  }

  getReactiveCustomers(): Observable<Array<Customer>> {
    let customers:Array<Customer> = new Array();
    return Observable.create(
      (observer) => {
        let eventSource = new EventSource(this.getReactiveCustomerUrl)
        eventSource.onmessage = (event) => {
          console.log('Received event: ', event)
          let json = JSON.parse(event.data)
          customers.push(new Customer(json['id'], json['firstName'], json['lastName']))
          // use NgZone to refresh angular's view when new item arrives
          this.zone.run(() => observer.next(customers)); 
        },
        eventSource.onerror = (error) => {
          // readyState === 0 (closed) means the remote source closed the connection,
          // so we can safely treat it as a normal situation. Another way of detecting the end of the stream
          // is to insert a special element in the stream of events, which the client can identify as the last one.
          if(eventSource.readyState === 0) {
            console.log('The stream has been closed by the server.');
            eventSource.close();
            observer.complete();
          } else {
            observer.error('EventSource error: ' + error);
          }
        }
      }
    )
  }

}
