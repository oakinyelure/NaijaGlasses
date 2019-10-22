import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormGroup, FormControl, Validators, AbstractControl } from '@angular/forms';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { SearchService } from '../core/services/search.service';
import { SearchResult, GetSearchResultResponse } from '../core/model/search-result.model';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit, OnDestroy {

  private _searchForm: FormGroup;

  private _$subscriptions: Array<Subscription> = [];

  private keyword: string;

  private _searchResults: Array<SearchResult> = [];

  constructor(
    private _route: ActivatedRoute,
    private _searchService: SearchService,
    private _router: Router
  ) { }


  ngOnInit() {
    this._bootstrapForm();
  }

  ngOnDestroy(): void {
    this._$subscriptions.forEach((subc: Subscription) => {
      subc.unsubscribe();
    });
  }

  private _bootstrapForm(): void {
    this._$subscriptions.push(
      this._route.queryParamMap.subscribe((param: Params) => {
        this.keyword = param.params.q;
        this._searchForm = new FormGroup({
          keyword: new FormControl(this.keyword,Validators.required)
        });
        this.search();
      })
    );

  }

  private _mountQueryParam():void {
    this._router.navigate(['search'], {queryParams: {q: this.searchKeyword.value}});
  }

  search(): void {
    if(this._searchForm.valid) {
      this._$subscriptions.push(
        this._searchService.search(this.keyword).subscribe((response: GetSearchResultResponse) => {
          console.log(response.result);
          this._searchResults = response.result;
        })
      );
    }
  }

  get searchKeyword(): AbstractControl {
    return this._searchForm.get('keyword');
  }

}
