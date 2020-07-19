
import { request } from 'http';

export class RTreeClient2 {

    private baseUrl: string;

    constructor(baseUrl?: string) {
        
        this.baseUrl = "http://localhost:8080";
        console.log("in constructor... baseUrl: " + baseUrl);
    }

    async getAll(): Promise<RTree2[]> {

        let url_ = this.baseUrl + "/rtree/get";

        const response = await fetch(
            url_
        );

        var trees: RTree2[] = new Array(0);

        interface MyObj {
            numDimensions: number;
            name: string;
        }
        let arr = await response.json();
        console.log("JSON str: ", arr);

        //let obj: string[] = JSON.parse(str);
        for (var i in arr) {
            var t = new RTree2();

            //let eachStr = arr[i];
            //console.log("JSON eachStr: ", eachStr);
            //let eachObj: MyObj = JSON.parse(eachStr);
            let eachObj = arr[i];

            t.name = eachObj.name;
            t.numDimensions = eachObj.numDimensions;
            trees.push(t);

        }
        return trees;
        //return response.json();
    }

    async get(name: string): Promise<RTree2> {
        let url_ = this.baseUrl + "/rtree/get/{name}";
        if (name === undefined || name === null)
            throw new Error("The parameter 'name' must be defined.");

        const response = await fetch(
            url_
        );

        return response.json();
    }
}

export class RTree2 {

    public name: string;
    public numDimensions: number;

}
