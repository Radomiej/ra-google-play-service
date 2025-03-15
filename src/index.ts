import { registerPlugin } from '@capacitor/core';

import type { PGServicePlugin } from './definitions';

const PGService = registerPlugin<PGServicePlugin>('PGService', {
  web: () => import('./web').then((m) => new m.PGServiceWeb()),
});

export * from './definitions';
export { PGService };

