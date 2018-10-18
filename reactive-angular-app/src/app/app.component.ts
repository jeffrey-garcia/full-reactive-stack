import { Component } from '@angular/core';
import { RestService } from './rest.service';
import { Observable } from 'rxjs/Observable';
import { tap } from 'rxjs/operators';

import { Customer } from './model/Customer';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Full Reactive Stack App';

  constructor(private restService: RestService) { }

  customer?:Customer

  // pass the customer list as Observable to HTML template
  customers?:Observable<Array<Customer>>

  ngOnInit(): void {
    //this.getCustomer()
    this.getReactiveCustomers()
  }

  getCustomer() {
    this.getCustomerWrapped().subscribe(
      (response) => {
        // no handling
      },
      (error) => {
        // error handle
      }
    )
  }

  getCustomerWrapped(): Observable<Customer> {
    return this.restService.getBlockingCustomer().pipe(
      tap(
        (response) => {
          console.log(response)
        },
        (error) => {
          console.log(error)
        }
      )
    )
  }

  getReactiveCustomers() {
    this.customers = this.restService.getReactiveCustomers()
  }

  // getReactiveCustomersWrapped(): Observable<Array<Customer>> {
  //   return this.restService.getReactiveCustomers().pipe(
  //     tap(
  //       (response) => {
  //         console.log(response)
  //       },
  //       (error) => {
  //         console.log(error)
  //       }
  //     )
  //   )
  // }

}
