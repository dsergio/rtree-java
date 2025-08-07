
export interface ITelemetryPage {

    displayPage: () => void;
    getId(): string;
    getPageObject(): {};
    setVirtual(isVirtual: boolean): void;
    getVirutal(): boolean;
}

abstract class TelemetryPage implements ITelemetryPage {
    id: string;
    pageName: string;
    isVirtual: boolean;

    constructor(pageName: string, id: string, isVirtual: boolean) {
        this.pageName = pageName;
        this.id = id;
        this.isVirtual = isVirtual;
    }
    setVirtual(isVirtual: boolean): void {
        this.isVirtual = isVirtual;
    }
    getVirutal(): boolean {
        return this.isVirtual;
    }

    getPageName(): string {
        return this.pageName;
    }

    displayPage(): void {
        console.log("pageName: " + this.pageName);
    }

    getId(): string {
        return this.id;
    }



    abstract getPageObject(): {};
}

export class TelemetryPageTMSGeneric extends TelemetryPage {

    constructor(pageName: string, id: string, isVirtual: boolean) {
        super(pageName, id, isVirtual);
    }

    getPageObject(): {} {
        let obj: any = {};

        obj["pageName"] = this.pageName;

        return obj;
    }
}

export class TelemetryPageGTM extends TelemetryPage {

    constructor(pageName: string, id: string, isVirtual: boolean) {
        super(pageName, id, isVirtual);
    }

    getPageObject(): {} {
        let obj: any = {};

        obj["id"] = this.id;
        obj["pageName"] = this.pageName;
        obj["isVirtual"] = this.isVirtual;

        return obj;
    }
}

export class TelemetryPageTealium extends TelemetryPage {

    constructor(pageName: string, id: string, isVirtual: boolean) {
        super(pageName, id, isVirtual);
    }

    getPageObject(): {} {
        let obj: any = {};

        obj["tealium_event"] = "view";
        obj["tealium_pageName"] = this.pageName;
        obj["isVirtual"] = this.isVirtual;

        return obj;
    }
}
