import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule, BOOTSTRAP_COMPONENTS_TOKEN } from './app/app.module';
import { environment } from './environments/environment';
import {AppComponent} from './app/app.component';

if (environment.production) {
  enableProdMode();
}

const components = [
  { type: AppComponent, selector: 'app-root#no1' },
  { type: AppComponent, selector: 'app-root#no2' }
];

const platform = platformBrowserDynamic([
  {provide: BOOTSTRAP_COMPONENTS_TOKEN, useValue: components}
]);
platform.bootstrapModule(AppModule);
