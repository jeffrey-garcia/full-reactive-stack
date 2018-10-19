import { Component } from '@angular/core';
import { RestService } from './rest.service';
import { Observable } from 'rxjs/Observable';
import { tap } from 'rxjs/operators';

import { Customer } from './model/Customer';
import { start } from 'repl';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Full Reactive Stack App';

  pageCount?:number = 0
  pageSize:number = 5

  customer?:Customer

  // pass the customer list as Observable to HTML template
  customers?:Observable<Array<Customer>>
  cachedCustomers:Array<Customer> = []

  constructor(private restService: RestService) { }

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

  getReactiveCustomers():void {
    this.customers = this.restService.getReactiveCustomers().pipe(
      tap(
        (response) => {
          this.cachedCustomers.push(response[response.length-1])
          console.log("cache count: " + this.cachedCustomers.length)
        }
      )
    )
  }

  getCachedCustomers(startIndex:number, endIndex:number):void {
    var getCached = (startIndex:number, endIndex:number) => {
      let customers:Array<Customer> = new Array();
      return Observable.create(
        (observer) => {
          let slicedCustomers = this.cachedCustomers.slice(startIndex, endIndex)
          customers = customers.concat(slicedCustomers)
          observer.next(customers)
        }
      )
    }
    this.customers = getCached(startIndex, endIndex)
  }

  getNext() {
    console.log(this.cachedCustomers)

    this.pageCount = this.pageCount + 1
    if (this.cachedCustomers!=null && this.cachedCustomers.length > (this.pageCount * this.pageSize)) {
      let startIndex = this.pageCount * this.pageSize
      let endIndex = this.pageCount * this.pageSize + this.pageSize
      this.getCachedCustomers(startIndex, endIndex)
    } else {
      this.getReactiveCustomers()
    }
  }

  getPrev() {
    console.log(this.cachedCustomers)

    this.pageCount = this.pageCount>0 ? this.pageCount-1 : 0
    if (this.cachedCustomers!=null && this.cachedCustomers.length > (this.pageCount * this.pageSize)) {
      let startIndex = this.pageCount * this.pageSize
      let endIndex = this.pageCount * this.pageSize + this.pageSize
      this.getCachedCustomers(startIndex, endIndex)
    }
  }

}
