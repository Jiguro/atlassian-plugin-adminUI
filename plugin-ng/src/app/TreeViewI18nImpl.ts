import {Injectable} from '@angular/core';
import {TreeviewI18nDefault, TreeviewItem} from 'ngx-treeview';

@Injectable()
export class TreeviewI18nImpl extends TreeviewI18nDefault {

  getText(checkededItems: TreeviewItem[], isAll: boolean): string {
    if (isAll) {
      return '[All options selected]';
    }

    if (checkededItems.length === 0) {
      return '...';
    } else {
      let concatenated = checkededItems[0].text;
      for (let i = 1; i < checkededItems.length; i++) {
        concatenated = concatenated + '; ' + checkededItems[i].text;
      }
      return concatenated;
    }
  }

}
