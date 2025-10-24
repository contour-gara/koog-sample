interface Event {
  readonly type: string;
}

interface MessageEvent<T = unknown> extends Event {
  readonly data: T;
}

declare class Worker {
  constructor(stringUrl: string | URL, options?: unknown);
  postMessage(message: unknown, transfer?: unknown[]): void;
  terminate(): void;
  onmessage: ((event: MessageEvent) => void) | null;
  onmessageerror: ((event: MessageEvent) => void) | null;
}
