import { NgModule, SkipSelf, Optional } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { MatIconRegistry } from '@angular/material';
import { DomSanitizer } from '@angular/platform-browser';
import { OAuthModule } from 'angular-oauth2-oidc';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './containers/app.component';
import { loadSvgResources } from '../utils/svg.util';

@NgModule({
  imports: [CommonModule, AppRoutingModule, HttpClientModule, OAuthModule.forRoot()],
  declarations: [AppComponent]
})
export class CoreModule {
  constructor(
    @Optional()
    @SkipSelf()
    parentModule: CoreModule,
    iconRegistry: MatIconRegistry,
    sanitizer: DomSanitizer
  ) {
    if (parentModule) {
      throw new Error('CoreModule 已经装载，请仅在 AppModule 中引入该模块。');
    }
    loadSvgResources(iconRegistry, sanitizer);
  }
}
