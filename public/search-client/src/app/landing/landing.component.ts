import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup, FormControl, Validators, AbstractControl } from '@angular/forms';
import { SearchService } from '../core/services/search.service';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss']
})
export class LandingComponent implements OnInit, OnDestroy {

  private _searchForm: FormGroup;

  private $_subscription: Array<Subscription> = [];

  constructor(
    private _searchService: SearchService,
    private _route: Router
  ) { }

  ngOnInit() {
    this._bootsrap();
  }

  ngOnDestroy(): void {
    this.$_subscription.forEach((subs: Subscription) => {
      subs.unsubscribe();
    });
  }

  private _bootsrap(): void {
    this._searchForm = new FormGroup({
      keyword: new FormControl(null, Validators.required)
    });
  }

  public search(): void {
    if(this._searchForm.valid) {
      let keyword = this.keyword.value;
      this._route.navigate(['search'], {queryParams: {q: keyword}});
    }
  }

  get keyword(): AbstractControl {
    return this._searchForm.get('keyword');
  }

}
