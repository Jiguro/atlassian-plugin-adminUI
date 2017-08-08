import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {TreeviewI18n, TreeviewModule} from 'ngx-treeview';
import {TreeviewI18nImpl} from './TreeViewI18nImpl';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    TreeviewModule.forRoot(),
    BrowserModule
  ],
  providers: [{provide: TreeviewI18n, useClass: TreeviewI18nImpl}],
  bootstrap: [AppComponent]
})
export class AppModule {
}
