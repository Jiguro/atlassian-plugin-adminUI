import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {TreeviewModule} from 'ngx-treeview';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    TreeviewModule.forRoot(),
    BrowserModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
