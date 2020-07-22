


export class Telemetry {
    config: TelemetryConfig;
    events: IEventDictionary = {};
    pages: IPageDictionary = {};
    size: number = 0;

    constructor(config: TelemetryConfig) {
        this.config = config;
    }

    addEvent(event: ITelementryEvent): void {
        this.events[event.getId()] = event;
        this.size++;
    }

    getEvent(eventId: string): ITelementryEvent {
        return this.events[eventId];
    }

    addPage(page: TelemetryPage): void {
        this.pages[page.id] = page;
    }

    getPage(pageId: string): TelemetryPage {
        return this.pages[pageId];
    }

    getEventsCount(): number {
        return this.size;
    }
}

abstract class TelemetryConfig {
    type: string;

    constructor(type: string) {
        this.type = type;
    }
}

export interface IEventDictionary {
    [key: string]: ITelementryEvent;
};
export interface IPageDictionary {
    [key: string]: TelemetryPage;
};


export class TelemetryConfigGA extends TelemetryConfig {
    tid: string;

    constructor(tid: string) {
        super("GA");
        this.tid = tid;
    }
}





abstract class TelemetryPage {
    id: string;
    pageName: string;

    constructor(pageName: string, id: string) {
        this.pageName = pageName;
        this.id = id;
    }

    getPageName(): string {
        return this.pageName;
    }
}

export class TelemetryPageGA extends TelemetryPage {

    constructor(pageName: string, id: string) {
        super(pageName, id);
    }
}







export interface ITelementryEvent {
    
    displayEvent: () => void;
    getId(): string;
    getTelemetryEventAttribute: (i: number) => string;
    getTelementryEventAttributeCount(): number;
}

abstract class TelemetryEvent implements ITelementryEvent {
    id: string;
    type: string;

    constructor(type: string, id: string) {
        this.type = type;
        this.id = id;
    }

    getId(): string {
        return this.id
    }

    abstract displayEvent(): void;
    abstract getTelemetryEventAttribute(i: number): string;
    abstract getTelementryEventAttributeCount(): number;
}

export class TelemetryEventGA extends TelemetryEvent {
    eventCategory: string;
    eventAction: string;
    eventLabel?: string;

 
    constructor(id: string, eventCategory: string, eventAction: string, eventLabel: string) {
        super("GA", id);
        this.eventCategory = eventCategory;
        this.eventAction = eventAction;
        this.eventLabel = eventLabel;
    }

    
    getTelementryEventAttributeCount(): number {
        return 3;
    }

    displayEvent(): void {
        console.log("id: " + this.id);
        console.log("type:" + this.type);
        console.log("eventCategory: " + this.eventCategory);
        console.log("eventAction: " + this.eventAction);
        if (this.eventLabel != null) {
            console.log("eventLabel: " + this.eventLabel);
        } else {
            console.log("eventLabel: " + "not set");
        }
    }

    getTelemetryEventAttribute(i: number): string {
        switch (i) {
            case 0: {
                return this.eventCategory;
            }
            case 1: {
                return this.eventAction;
            }
            case 2: {
                if (this.eventLabel != null) {
                    return this.eventLabel;
                } else {
                    return "";
                }
            }
        }
    }
}

