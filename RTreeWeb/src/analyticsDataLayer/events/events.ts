

export interface ITelemetryEvent {

    displayEvent: () => void;
    getId(): string;
    getTelemetryEventAttribute: (i: number) => string;
    getTelementryEventAttributeCount(): number;
    addEventAttribute(eventAttributeIndex: number, eventAttribute: string): void;
    removeEventAttribute(eventAttributeIndex: number): void;
}

abstract class TelemetryEvent implements ITelemetryEvent {
    id: string;
    type: string;

    constructor(type: string, id: string) {
        this.type = type;
        this.id = id;
    }

    getId(): string {
        return this.id;
    }

    abstract displayEvent(): void;
    abstract getTelemetryEventAttribute(i: number): string;
    abstract getTelementryEventAttributeCount(): number;
    abstract addEventAttribute(eventAttributeIndex: number, eventAttribute: string): void;
    abstract removeEventAttribute(eventAttributeIndex: number): void;
}

export class TelemetryEventTMSGeneric extends TelemetryEvent {
    eventAttributes: any = {};
    eventAttributeSize: number = 0;

    constructor(id: string) {
        super("EventTMSGeneric", id);
    }

    addEventAttribute(eventAttributeIndex: number, eventAttribute: string): void {
        this.eventAttributes[eventAttributeIndex] = eventAttribute;
        this.eventAttributeSize++;
    }

    removeEventAttribute(eventAttributeIndex: number): void {
        if (eventAttributeIndex in this.eventAttributes) {
            delete this.eventAttributes[eventAttributeIndex];
            this.eventAttributeSize--;
        }
    }

    getTelementryEventAttributeCount(): number {
        return this.eventAttributeSize;
    }

    displayEvent(): void {
        console.log("id: " + this.id);
        console.log("type:" + this.type);
        
    }

    getTelemetryEventAttribute(i: number): string {
        return this.eventAttributes[i];
    }
}