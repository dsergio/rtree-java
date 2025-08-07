
import { ITelemetryEvent } from './events/events';
import { ITelemetryPage, TelemetryPageTMSGeneric } from './pages/pages';

export * from './events/events';
export * from './pages/pages';

export interface IEventDictionary {
    [key: string]: ITelemetryEvent;
};
export interface IPageDictionary {
    [key: string]: ITelemetryPage;
};
export interface IDataLayerDictionary {
    [key: string]: ITelemetryDataLayer;
};


export class Telemetry {
    telemetryId: string;
    dataLayers: IDataLayerDictionary = {};
    dataLayersSize: number = 0;

    constructor(telemetryId: string) {
        this.telemetryId = telemetryId;
    }

    addDataLayer(dataLayer: ITelemetryDataLayer): void {
        this.dataLayers[dataLayer.getId()] = dataLayer;
        this.dataLayersSize++;
    }

    getDataLayer(dataLayerId: string): ITelemetryDataLayer {
        return this.dataLayers[dataLayerId];
    }

    getDataLayersCount(): number {
        return this.dataLayersSize;
    }

    generateDataLayer(): string {

        let ret: any = {};

        ret["telemetryId"] = this.telemetryId;
        ret["dataLayerObjects"] = {};

        for (let key in this.dataLayers) {
            let value = this.dataLayers[key];
            ret["dataLayerObjects"][key] = value.generateDataLayer();
        }

        return JSON.stringify(ret);
    }
}

export interface ITelemetryDataLayer {
    getId(): string;
    generateDataLayer(): {};
    generatePageDataLayer(): {};
    getRootObject(): string;
    setPage(page: ITelemetryPage): void;
    addEvent(event: ITelemetryEvent): void;
    removeEvent(eventId: string): void;
    getEvent(eventId: string): ITelemetryEvent;
    clearEvents(): void;
}


export class DataLayerTMSGeneric implements ITelemetryDataLayer {
    dataLayerId: string;
    events: IEventDictionary = {};
    eventSize: number = 0;
    page: ITelemetryPage;

    constructor(dataLayerId: string) {
        this.dataLayerId = dataLayerId;
        this.page = new TelemetryPageTMSGeneric("Default Page", "default-page", false);
    }

    clearEvents() {
        this.events = {};
    }

    getRootObject(): string {
        return "DataLayerTMSGeneric"
    }

    getId(): string {
        return this.dataLayerId
    }

    addEvent(event: ITelemetryEvent): void {
        this.events[event.getId()] = event;
        this.eventSize++;
    }

    removeEvent(eventId: string): void {
        if (eventId in this.events) {
            delete this.events[eventId];
            this.eventSize--;
        }
    }

    getEvent(eventId: string): ITelemetryEvent {
        return this.events[eventId];
    }

    setPage(page: ITelemetryPage): void {
        this.page = page;
    }

    getPage(): ITelemetryPage {
        return this.page;
    }

    getEventsCount(): number {
        return this.eventSize;
    }

    generateDataLayer(): string {

        let obj: any = {};

        obj[this.getRootObject()] = {};

        obj[this.getRootObject()]["dataLayerId"] = this.dataLayerId;
        obj[this.getRootObject()]["page"] = this.page.getPageObject();

        obj[this.getRootObject()]["events"] = {};
        for (let key in this.events) {
            let value = this.events[key];
            obj[this.getRootObject()]["events"] = value.getId();
        }

        return JSON.stringify(obj);
    }

    generatePageDataLayer(): {} {

        return this.page.getPageObject();
    }

}

export class DataLayerGTM extends DataLayerTMSGeneric {

    constructor(dataLayerId: string) {
        super("DataLayerGTM");
        this.dataLayerId = dataLayerId;
    }

    getRootObject(): string {
        return "gtmDataLayer";
    }
}

export class DataLayerTealium extends DataLayerTMSGeneric {

    constructor(dataLayerId: string) {
        super("DataLayerTealium");
        this.dataLayerId = dataLayerId;
    }

    getRootObject(): string {
        return "utag_data";
    }
}



declare global {
    interface Window {
        dataLayerCollection: any;
        gtmDataLayer: any;
        dataLayer: any;
        utag_data: any;
    }
}

export function attachDataLayerObjects(telemetry: Telemetry) {

    var dataLayerCollection: any = {};
    dataLayerCollection["dataLayerTMSGeneric"] = telemetry.getDataLayer("Generic").generatePageDataLayer();
    dataLayerCollection["dataLayerGTM"] = telemetry.getDataLayer("GTM").generatePageDataLayer();
    dataLayerCollection["dataLayerTealium"] = telemetry.getDataLayer("Tealium").generatePageDataLayer();


    window.dataLayerCollection = dataLayerCollection || {};

    if (window.gtmDataLayer) {
        for (var i in dataLayerCollection["dataLayerGTM"]) {
            window.gtmDataLayer[i] = dataLayerCollection["dataLayerGTM"][i];
        }
    } else {
        window.gtmDataLayer =  dataLayerCollection["dataLayerGTM"];
    }

    if (dataLayerCollection["dataLayerGTM"]["isVirtual"]) {
        // window.dataLayer = dataLayerCollection["dataLayerGTM"];
        // window.dataLayer = window.dataLayer || [];
        window.dataLayer.push({ 'event': dataLayerCollection["dataLayerGTM"]['id'] });
    }

    if (window.utag_data) {
        for (var i in dataLayerCollection["dataLayerTealium"]) {
            window.utag_data[i] = dataLayerCollection["dataLayerTealium"][i];
        }
    } else {
        window.utag_data = dataLayerCollection["dataLayerTealium"];
    }


    console.log("dataLayerCollection: ", dataLayerCollection);
}