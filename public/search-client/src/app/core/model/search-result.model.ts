import { IdentityProperty } from "./identity-response.model";

export class SearchResult {
    id: IdentityProperty;
    images: Array<string> = [];
    links: Array<string> = [];
    contents: Array<string> = [];
    url: string;
    searchHitCount: number;
    title: string;
    possibleSearchContent: string;
}

export interface GetSearchResultResponse {
    result: Array<SearchResult>;
}