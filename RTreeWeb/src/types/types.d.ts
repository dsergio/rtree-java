declare module '@/two/two-app-main.js' {
  // Option A: loose typing
  const value: any;
  export = value;

  // OR Option B: strict typing example
  // export function initTwoApp(): void;
}

declare module 'jquery' {
  // Option A: loose typing
  const $: any;
  export = $;

  // OR Option B: strict typing example
  // export function $(selector: string): JQuery;
}