﻿/* tslint:disable */
/* eslint-disable */
//----------------------
// <auto-generated>
//     Generated using the NSwag toolchain v13.2.2.0 (NJsonSchema v10.1.4.0 (Newtonsoft.Json v11.0.0.0)) (http://NSwag.org)
// </auto-generated>
//----------------------
// ReSharper disable InconsistentNaming

export interface IRTreeClient {
    /**
     * RTree_getAll
     * @return OK
     */
    getAll(): Promise<RTree[]>;
    /**
     * RTree_newTree
     * @param rtreeCreate rtreeCreate
     * @return OK
     */
    newTree(rtreeCreate: RTreeCreate): Promise<RTree>;
    /**
     * RTree_search
     * @param searchRectangle searchRectangle
     * @param treeName treeName
     * @return OK
     */
    search(searchRectangle: SearchRectangle, treeName: string): Promise<{ [key: string]: ILocationItem[]; }>;
    /**
     * RTree_get
     * @param treeName treeName
     * @return OK
     */
    get(treeName: string): Promise<RTree>;
    /**
     * RTree_insert
     * @param item item
     * @param treeName treeName
     * @return OK
     */
    insert(item: LocationItem, treeName: string): Promise<RTree>;
}

export class RTreeClient implements IRTreeClient {
    private http: { fetch(url: RequestInfo, init?: RequestInit): Promise<Response> };
    private baseUrl: string;
    protected jsonParseReviver: ((key: string, value: any) => any) | undefined = undefined;

    constructor(baseUrl?: string, http?: { fetch(url: RequestInfo, init?: RequestInit): Promise<Response> }) {
        this.http = http ? http : <any>window;
        this.baseUrl = baseUrl ? baseUrl : "localhost:8080/";
    }

    /**
     * RTree_getAll
     * @return OK
     */
    getAll(): Promise<RTree[]> {
        let url_ = this.baseUrl + "/api/RTree/";
        url_ = url_.replace(/[?&]$/, "");

        let options_ = <RequestInit>{
            method: "GET",
            headers: {
                "Accept": "application/json"
            }
        };

        return this.http.fetch(url_, options_).then((_response: Response) => {
            return this.processGetAll(_response);
        });
    }

    protected processGetAll(response: Response): Promise<RTree[]> {
        const status = response.status;
        let _headers: any = {}; if (response.headers && response.headers.forEach) { response.headers.forEach((v: any, k: any) => _headers[k] = v); };
        if (status === 200) {
            return response.text().then((_responseText) => {
                let result200: any = null;
                let resultData200 = _responseText === "" ? null : JSON.parse(_responseText, this.jsonParseReviver);
                if (Array.isArray(resultData200)) {
                    result200 = [] as any;
                    for (let item of resultData200)
                        result200!.push(RTree.fromJS(item));
                }
                return result200;
            });
        } else if (status === 401) {
            return response.text().then((_responseText) => {
                return throwException("Unauthorized", status, _responseText, _headers);
            });
        } else if (status === 403) {
            return response.text().then((_responseText) => {
                return throwException("Forbidden", status, _responseText, _headers);
            });
        } else if (status === 404) {
            return response.text().then((_responseText) => {
                return throwException("Not Found", status, _responseText, _headers);
            });
        } else if (status !== 200 && status !== 204) {
            return response.text().then((_responseText) => {
                return throwException("An unexpected server error occurred.", status, _responseText, _headers);
            });
        }
        return Promise.resolve<RTree[]>(<any>null);
    }

    /**
     * RTree_newTree
     * @param rtreeCreate rtreeCreate
     * @return OK
     */
    newTree(rtreeCreate: RTreeCreate): Promise<RTree> {
        let url_ = this.baseUrl + "/api/RTree/";
        url_ = url_.replace(/[?&]$/, "");

        const content_ = JSON.stringify(rtreeCreate);

        let options_ = <RequestInit>{
            body: content_,
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            }
        };

        return this.http.fetch(url_, options_).then((_response: Response) => {
            return this.processNewTree(_response);
        });
    }

    protected processNewTree(response: Response): Promise<RTree> {
        const status = response.status;
        let _headers: any = {}; if (response.headers && response.headers.forEach) { response.headers.forEach((v: any, k: any) => _headers[k] = v); };
        if (status === 200) {
            return response.text().then((_responseText) => {
                let result200: any = null;
                let resultData200 = _responseText === "" ? null : JSON.parse(_responseText, this.jsonParseReviver);
                result200 = RTree.fromJS(resultData200);
                return result200;
            });
        } else if (status === 201) {
            return response.text().then((_responseText) => {
                return throwException("Created", status, _responseText, _headers);
            });
        } else if (status === 401) {
            return response.text().then((_responseText) => {
                return throwException("Unauthorized", status, _responseText, _headers);
            });
        } else if (status === 403) {
            return response.text().then((_responseText) => {
                return throwException("Forbidden", status, _responseText, _headers);
            });
        } else if (status === 404) {
            return response.text().then((_responseText) => {
                return throwException("Not Found", status, _responseText, _headers);
            });
        } else if (status !== 200 && status !== 204) {
            return response.text().then((_responseText) => {
                return throwException("An unexpected server error occurred.", status, _responseText, _headers);
            });
        }
        return Promise.resolve<RTree>(<any>null);
    }

    /**
     * RTree_search
     * @param searchRectangle searchRectangle
     * @param treeName treeName
     * @return OK
     */
    search(searchRectangle: SearchRectangle, treeName: string): Promise<{ [key: string]: ILocationItem[]; }> {
        let url_ = this.baseUrl + "/api/RTree/search/{treeName}";
        if (treeName === undefined || treeName === null)
            throw new Error("The parameter 'treeName' must be defined.");
        url_ = url_.replace("{treeName}", encodeURIComponent("" + treeName));
        url_ = url_.replace(/[?&]$/, "");

        const content_ = JSON.stringify(searchRectangle);

        let options_ = <RequestInit>{
            body: content_,
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            }
        };

        return this.http.fetch(url_, options_).then((_response: Response) => {
            return this.processSearch(_response);
        });
    }

    protected processSearch(response: Response): Promise<{ [key: string]: ILocationItem[]; }> {
        const status = response.status;
        let _headers: any = {}; if (response.headers && response.headers.forEach) { response.headers.forEach((v: any, k: any) => _headers[k] = v); };
        if (status === 200) {
            return response.text().then((_responseText) => {
                let result200: any = null;
                let resultData200 = _responseText === "" ? null : JSON.parse(_responseText, this.jsonParseReviver);
                if (resultData200) {
                    result200 = {} as any;
                    for (let key in resultData200) {
                        if (resultData200.hasOwnProperty(key))
                            result200![key] = resultData200[key] ? resultData200[key].map((i: any) => ILocationItem.fromJS(i)) : [];
                    }
                }
                return result200;
            });
        } else if (status === 201) {
            return response.text().then((_responseText) => {
                return throwException("Created", status, _responseText, _headers);
            });
        } else if (status === 401) {
            return response.text().then((_responseText) => {
                return throwException("Unauthorized", status, _responseText, _headers);
            });
        } else if (status === 403) {
            return response.text().then((_responseText) => {
                return throwException("Forbidden", status, _responseText, _headers);
            });
        } else if (status === 404) {
            return response.text().then((_responseText) => {
                return throwException("Not Found", status, _responseText, _headers);
            });
        } else if (status !== 200 && status !== 204) {
            return response.text().then((_responseText) => {
                return throwException("An unexpected server error occurred.", status, _responseText, _headers);
            });
        }
        return Promise.resolve<{ [key: string]: ILocationItem[]; }>(<any>null);
    }

    /**
     * RTree_get
     * @param treeName treeName
     * @return OK
     */
    get(treeName: string): Promise<RTree> {
        let url_ = this.baseUrl + "/api/RTree/{treeName}";
        if (treeName === undefined || treeName === null)
            throw new Error("The parameter 'treeName' must be defined.");
        url_ = url_.replace("{treeName}", encodeURIComponent("" + treeName));
        url_ = url_.replace(/[?&]$/, "");

        let options_ = <RequestInit>{
            method: "GET",
            headers: {
                "Accept": "application/json"
            }
        };

        return this.http.fetch(url_, options_).then((_response: Response) => {
            return this.processGet(_response);
        });
    }

    protected processGet(response: Response): Promise<RTree> {
        const status = response.status;
        let _headers: any = {}; if (response.headers && response.headers.forEach) { response.headers.forEach((v: any, k: any) => _headers[k] = v); };
        if (status === 200) {
            return response.text().then((_responseText) => {
                let result200: any = null;
                let resultData200 = _responseText === "" ? null : JSON.parse(_responseText, this.jsonParseReviver);
                result200 = RTree.fromJS(resultData200);
                return result200;
            });
        } else if (status === 401) {
            return response.text().then((_responseText) => {
                return throwException("Unauthorized", status, _responseText, _headers);
            });
        } else if (status === 403) {
            return response.text().then((_responseText) => {
                return throwException("Forbidden", status, _responseText, _headers);
            });
        } else if (status === 404) {
            return response.text().then((_responseText) => {
                return throwException("Not Found", status, _responseText, _headers);
            });
        } else if (status !== 200 && status !== 204) {
            return response.text().then((_responseText) => {
                return throwException("An unexpected server error occurred.", status, _responseText, _headers);
            });
        }
        return Promise.resolve<RTree>(<any>null);
    }

    /**
     * RTree_insert
     * @param item item
     * @param treeName treeName
     * @return OK
     */
    insert(item: LocationItem, treeName: string): Promise<RTree> {
        let url_ = this.baseUrl + "/api/RTree/{treeName}";
        if (treeName === undefined || treeName === null)
            throw new Error("The parameter 'treeName' must be defined.");
        url_ = url_.replace("{treeName}", encodeURIComponent("" + treeName));
        url_ = url_.replace(/[?&]$/, "");

        const content_ = JSON.stringify(item);

        let options_ = <RequestInit>{
            body: content_,
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Accept": "application/json"
            }
        };

        return this.http.fetch(url_, options_).then((_response: Response) => {
            return this.processInsert(_response);
        });
    }

    protected processInsert(response: Response): Promise<RTree> {
        const status = response.status;
        let _headers: any = {}; if (response.headers && response.headers.forEach) { response.headers.forEach((v: any, k: any) => _headers[k] = v); };
        if (status === 200) {
            return response.text().then((_responseText) => {
                let result200: any = null;
                let resultData200 = _responseText === "" ? null : JSON.parse(_responseText, this.jsonParseReviver);
                result200 = RTree.fromJS(resultData200);
                return result200;
            });
        } else if (status === 201) {
            return response.text().then((_responseText) => {
                return throwException("Created", status, _responseText, _headers);
            });
        } else if (status === 401) {
            return response.text().then((_responseText) => {
                return throwException("Unauthorized", status, _responseText, _headers);
            });
        } else if (status === 403) {
            return response.text().then((_responseText) => {
                return throwException("Forbidden", status, _responseText, _headers);
            });
        } else if (status === 404) {
            return response.text().then((_responseText) => {
                return throwException("Not Found", status, _responseText, _headers);
            });
        } else if (status !== 200 && status !== 204) {
            return response.text().then((_responseText) => {
                return throwException("An unexpected server error occurred.", status, _responseText, _headers);
            });
        }
        return Promise.resolve<RTree>(<any>null);
    }
}

export class IHyperRectangle implements IIHyperRectangle {
    dimensionArray1?: number[] | undefined;
    dimensionArray2?: number[] | undefined;
    json?: { [key: string]: any; } | undefined;
    level?: number | undefined;
    numberDimensions?: number | undefined;
    space?: number | undefined;

    constructor(data?: IIHyperRectangle) {
        if (data) {
            for (var property in data) {
                if (data.hasOwnProperty(property))
                    (<any>this)[property] = (<any>data)[property];
            }
        }
    }

    init(_data?: any) {
        if (_data) {
            if (Array.isArray(_data["dimensionArray1"])) {
                this.dimensionArray1 = [] as any;
                for (let item of _data["dimensionArray1"])
                    this.dimensionArray1!.push(item);
            }
            if (Array.isArray(_data["dimensionArray2"])) {
                this.dimensionArray2 = [] as any;
                for (let item of _data["dimensionArray2"])
                    this.dimensionArray2!.push(item);
            }
            if (_data["json"]) {
                this.json = {} as any;
                for (let key in _data["json"]) {
                    if (_data["json"].hasOwnProperty(key))
                        this.json![key] = _data["json"][key];
                }
            }
            this.level = _data["level"];
            this.numberDimensions = _data["numberDimensions"];
            this.space = _data["space"];
        }
    }

    static fromJS(data: any): IHyperRectangle {
        data = typeof data === 'object' ? data : {};
        let result = new IHyperRectangle();
        result.init(data);
        return result;
    }

    toJSON(data?: any) {
        data = typeof data === 'object' ? data : {};
        if (Array.isArray(this.dimensionArray1)) {
            data["dimensionArray1"] = [];
            for (let item of this.dimensionArray1)
                data["dimensionArray1"].push(item);
        }
        if (Array.isArray(this.dimensionArray2)) {
            data["dimensionArray2"] = [];
            for (let item of this.dimensionArray2)
                data["dimensionArray2"].push(item);
        }
        if (this.json) {
            data["json"] = {};
            for (let key in this.json) {
                if (this.json.hasOwnProperty(key))
                    data["json"][key] = this.json[key];
            }
        }
        data["level"] = this.level;
        data["numberDimensions"] = this.numberDimensions;
        data["space"] = this.space;
        return data;
    }
}

export interface IIHyperRectangle {
    dimensionArray1?: number[] | undefined;
    dimensionArray2?: number[] | undefined;
    json?: { [key: string]: any; } | undefined;
    level?: number | undefined;
    numberDimensions?: number | undefined;
    space?: number | undefined;
}

export class ILocationItem implements IILocationItem {
    dimensionArray?: number[] | undefined;
    id?: string | undefined;
    json?: { [key: string]: any; } | undefined;
    locationJson?: { [key: string]: any; } | undefined;
    numberDimensions?: number | undefined;
    type?: string | undefined;

    constructor(data?: IILocationItem) {
        if (data) {
            for (var property in data) {
                if (data.hasOwnProperty(property))
                    (<any>this)[property] = (<any>data)[property];
            }
        }
    }

    init(_data?: any) {
        if (_data) {
            if (Array.isArray(_data["dimensionArray"])) {
                this.dimensionArray = [] as any;
                for (let item of _data["dimensionArray"])
                    this.dimensionArray!.push(item);
            }
            this.id = _data["id"];
            if (_data["json"]) {
                this.json = {} as any;
                for (let key in _data["json"]) {
                    if (_data["json"].hasOwnProperty(key))
                        this.json![key] = _data["json"][key];
                }
            }
            if (_data["locationJson"]) {
                this.locationJson = {} as any;
                for (let key in _data["locationJson"]) {
                    if (_data["locationJson"].hasOwnProperty(key))
                        this.locationJson![key] = _data["locationJson"][key];
                }
            }
            this.numberDimensions = _data["numberDimensions"];
            this.type = _data["type"];
        }
    }

    static fromJS(data: any): ILocationItem {
        data = typeof data === 'object' ? data : {};
        let result = new ILocationItem();
        result.init(data);
        return result;
    }

    toJSON(data?: any) {
        data = typeof data === 'object' ? data : {};
        if (Array.isArray(this.dimensionArray)) {
            data["dimensionArray"] = [];
            for (let item of this.dimensionArray)
                data["dimensionArray"].push(item);
        }
        data["id"] = this.id;
        if (this.json) {
            data["json"] = {};
            for (let key in this.json) {
                if (this.json.hasOwnProperty(key))
                    data["json"][key] = this.json[key];
            }
        }
        if (this.locationJson) {
            data["locationJson"] = {};
            for (let key in this.locationJson) {
                if (this.locationJson.hasOwnProperty(key))
                    data["locationJson"][key] = this.locationJson[key];
            }
        }
        data["numberDimensions"] = this.numberDimensions;
        data["type"] = this.type;
        return data;
    }
}

export interface IILocationItem {
    dimensionArray?: number[] | undefined;
    id?: string | undefined;
    json?: { [key: string]: any; } | undefined;
    locationJson?: { [key: string]: any; } | undefined;
    numberDimensions?: number | undefined;
    type?: string | undefined;
}

export class LocationItem implements ILocationItem {
    dimensionArray?: number[] | undefined;
    itemId?: string | undefined;
    itemType?: string | undefined;
    numberDimensions?: number | undefined;

    constructor(data?: ILocationItem) {
        if (data) {
            for (var property in data) {
                if (data.hasOwnProperty(property))
                    (<any>this)[property] = (<any>data)[property];
            }
        }
    }

    init(_data?: any) {
        if (_data) {
            if (Array.isArray(_data["dimensionArray"])) {
                this.dimensionArray = [] as any;
                for (let item of _data["dimensionArray"])
                    this.dimensionArray!.push(item);
            }
            this.itemId = _data["itemId"];
            this.itemType = _data["itemType"];
            this.numberDimensions = _data["numberDimensions"];
        }
    }

    static fromJS(data: any): LocationItem {
        data = typeof data === 'object' ? data : {};
        let result = new LocationItem();
        result.init(data);
        return result;
    }

    toJSON(data?: any) {
        data = typeof data === 'object' ? data : {};
        if (Array.isArray(this.dimensionArray)) {
            data["dimensionArray"] = [];
            for (let item of this.dimensionArray)
                data["dimensionArray"].push(item);
        }
        data["itemId"] = this.itemId;
        data["itemType"] = this.itemType;
        data["numberDimensions"] = this.numberDimensions;
        return data;
    }
}

export interface ILocationItem {
    dimensionArray?: number[] | undefined;
    itemId?: string | undefined;
    itemType?: string | undefined;
    numberDimensions?: number | undefined;
}

export class RTree implements IRTree {
    name?: string | undefined;
    numDimensions?: number | undefined;
    points?: ILocationItem[] | undefined;
    rectangles?: IHyperRectangle[] | undefined;

    constructor(data?: IRTree) {
        if (data) {
            for (var property in data) {
                if (data.hasOwnProperty(property))
                    (<any>this)[property] = (<any>data)[property];
            }
        }
    }

    init(_data?: any) {
        if (_data) {
            this.name = _data["name"];
            this.numDimensions = _data["numDimensions"];
            if (Array.isArray(_data["points"])) {
                this.points = [] as any;
                for (let item of _data["points"])
                    this.points!.push(ILocationItem.fromJS(item));
            }
            if (Array.isArray(_data["rectangles"])) {
                this.rectangles = [] as any;
                for (let item of _data["rectangles"])
                    this.rectangles!.push(IHyperRectangle.fromJS(item));
            }
        }
    }

    static fromJS(data: any): RTree {
        data = typeof data === 'object' ? data : {};
        let result = new RTree();
        result.init(data);
        return result;
    }

    toJSON(data?: any) {
        data = typeof data === 'object' ? data : {};
        data["name"] = this.name;
        data["numDimensions"] = this.numDimensions;
        if (Array.isArray(this.points)) {
            data["points"] = [];
            for (let item of this.points)
                data["points"].push(item.toJSON());
        }
        if (Array.isArray(this.rectangles)) {
            data["rectangles"] = [];
            for (let item of this.rectangles)
                data["rectangles"].push(item.toJSON());
        }
        return data;
    }
}

export interface IRTree {
    name?: string | undefined;
    numDimensions?: number | undefined;
    points?: ILocationItem[] | undefined;
    rectangles?: IHyperRectangle[] | undefined;
}

export class RTreeCreate implements IRTreeCreate {
    maxChildren?: number | undefined;
    maxItems?: number | undefined;
    numDimensions?: number | undefined;
    treeName?: string | undefined;

    constructor(data?: IRTreeCreate) {
        if (data) {
            for (var property in data) {
                if (data.hasOwnProperty(property))
                    (<any>this)[property] = (<any>data)[property];
            }
        }
    }

    init(_data?: any) {
        if (_data) {
            this.maxChildren = _data["maxChildren"];
            this.maxItems = _data["maxItems"];
            this.numDimensions = _data["numDimensions"];
            this.treeName = _data["treeName"];
        }
    }

    static fromJS(data: any): RTreeCreate {
        data = typeof data === 'object' ? data : {};
        let result = new RTreeCreate();
        result.init(data);
        return result;
    }

    toJSON(data?: any) {
        data = typeof data === 'object' ? data : {};
        data["maxChildren"] = this.maxChildren;
        data["maxItems"] = this.maxItems;
        data["numDimensions"] = this.numDimensions;
        data["treeName"] = this.treeName;
        return data;
    }
}

export interface IRTreeCreate {
    maxChildren?: number | undefined;
    maxItems?: number | undefined;
    numDimensions?: number | undefined;
    treeName?: string | undefined;
}

export class SearchRectangle implements ISearchRectangle {
    dimensionArray1?: number[] | undefined;
    dimensionArray2?: number[] | undefined;
    numberDimensions?: number | undefined;

    constructor(data?: ISearchRectangle) {
        if (data) {
            for (var property in data) {
                if (data.hasOwnProperty(property))
                    (<any>this)[property] = (<any>data)[property];
            }
        }
    }

    init(_data?: any) {
        if (_data) {
            if (Array.isArray(_data["dimensionArray1"])) {
                this.dimensionArray1 = [] as any;
                for (let item of _data["dimensionArray1"])
                    this.dimensionArray1!.push(item);
            }
            if (Array.isArray(_data["dimensionArray2"])) {
                this.dimensionArray2 = [] as any;
                for (let item of _data["dimensionArray2"])
                    this.dimensionArray2!.push(item);
            }
            this.numberDimensions = _data["numberDimensions"];
        }
    }

    static fromJS(data: any): SearchRectangle {
        data = typeof data === 'object' ? data : {};
        let result = new SearchRectangle();
        result.init(data);
        return result;
    }

    toJSON(data?: any) {
        data = typeof data === 'object' ? data : {};
        if (Array.isArray(this.dimensionArray1)) {
            data["dimensionArray1"] = [];
            for (let item of this.dimensionArray1)
                data["dimensionArray1"].push(item);
        }
        if (Array.isArray(this.dimensionArray2)) {
            data["dimensionArray2"] = [];
            for (let item of this.dimensionArray2)
                data["dimensionArray2"].push(item);
        }
        data["numberDimensions"] = this.numberDimensions;
        return data;
    }
}

export interface ISearchRectangle {
    dimensionArray1?: number[] | undefined;
    dimensionArray2?: number[] | undefined;
    numberDimensions?: number | undefined;
}

export class ApiException extends Error {
    message: string;
    status: number;
    response: string;
    headers: { [key: string]: any; };
    result: any;

    constructor(message: string, status: number, response: string, headers: { [key: string]: any; }, result: any) {
        super();

        this.message = message;
        this.status = status;
        this.response = response;
        this.headers = headers;
        this.result = result;
    }

    protected isApiException = true;

    static isApiException(obj: any): obj is ApiException {
        return obj.isApiException === true;
    }
}

function throwException(message: string, status: number, response: string, headers: { [key: string]: any; }, result?: any): any {
    if (result !== null && result !== undefined)
        throw result;
    else
        throw new ApiException(message, status, response, headers, null);
}