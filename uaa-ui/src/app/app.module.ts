import { BrowserModule, DomSanitizer } from '@angular/platform-browser';
import { NgModule, Optional, SkipSelf } from '@angular/core';

import { AppComponent } from './core/containers/app.component';
import { CoreModule } from './core/core.module';

@NgModule({
  declarations: [AppComponent],
  imports: [BrowserModule, CoreModule],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
