import {BrowserModule} from '@angular/platform-browser';
import {ApplicationRef, ComponentFactoryResolver, Inject, NgModule, OpaqueToken, Type} from '@angular/core';

import {AppComponent} from './app.component';
import {TreeviewI18n, TreeviewModule} from 'ngx-treeview';
import {TreeviewI18nImpl} from './TreeViewI18nImpl';

export const BOOTSTRAP_COMPONENTS_TOKEN = new OpaqueToken('bootstrap_components');

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    TreeviewModule.forRoot(),
    BrowserModule
  ],
  providers: [{provide: TreeviewI18n, useClass: TreeviewI18nImpl}],
  entryComponents: [AppComponent]
})
export class AppModule {
  constructor(private resolver: ComponentFactoryResolver,
              @Inject(BOOTSTRAP_COMPONENTS_TOKEN) private components) {}

  ngDoBootstrap(appRef: ApplicationRef) {
    this.components.forEach((componentDef: {type: Type<any>, selector: string}) => {
      const factory = this.resolver.resolveComponentFactory(componentDef.type);
      factory['factory'].selector = componentDef.selector;
      appRef.bootstrap(factory);
    });
  }
}
