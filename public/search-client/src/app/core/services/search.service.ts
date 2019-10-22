import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { GetSearchResultResponse, SearchResult } from '../model/search-result.model';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  constructor(private _http: HttpClient) { }

  search(keyword: string): Observable<GetSearchResultResponse> {
    const URL = `http://localhost:8081/search?q=${keyword}`;
    return this._http.get<GetSearchResultResponse>(URL).pipe(
      tap((response: GetSearchResultResponse) => {
        response.result.forEach((result: SearchResult) => {
          let possibleSearchContent = result.contents.filter(content => content.toLowerCase().includes(keyword.toLowerCase()));
          if(possibleSearchContent.length) {
            result.possibleSearchContent = possibleSearchContent.join(" ... ");
            
          }
        });
        return response.result;
      })
    );
  }
}
