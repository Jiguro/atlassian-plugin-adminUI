import * as _ from 'lodash';
import {Component} from '@angular/core';

import {TreeviewComponent, TreeviewConfig, TreeviewItem} from 'ngx-treeview';
import {TreeviewItemComponent} from 'ngx-treeview/src/treeview-item.component';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title: String = 'Demo App';
  treeSelectConfig: TreeviewConfig = TreeviewConfig.create({
    hasAllCheckBox: false,
    hasFilter: true,
    hasCollapseExpand: false
  });
  treeSelectItems: TreeviewItem[] = [];

  constructor() {
    // Module ngx-treeview only counts items without children as selected when checked.
    // Override this behaviour so that both items with children or without count as selected when checked
    TreeviewItem.prototype.getCheckedItems = function () {
      let checkedItems = [];
      if (this.internalChecked) {
        checkedItems.push(this);
      }

      if (!_.isNil(this.internalChildren)) {
        const childCount = this.internalChildren.length;
        for (let i = 0; i < childCount; i++) {
          checkedItems = _.concat(checkedItems, this.internalChildren[i].getCheckedItems());
        }
      }
      return checkedItems;
    };

    // Module ngx-treeview automatically checks/unchecks all children when checking/unchecking a parent item.
    // Override this behaviour so that this automatic checking/unchecking of children does not take place.
    TreeviewItem.prototype.setCheckedRecursive = function (value) {
    };

    // Module ngx-treeview automatically checks parent when all children are checked (and unchecks parent when at least one child is unchecked).
    // Override this behaviour so that this automatic checking/unchecking of parents does not take place
    TreeviewItemComponent.prototype.onChildCheckedChange = function (child, checked) {
      this.checkedChange.emit(checked);
    };

    let alreadyPatchedUpdateRefChecked = false;
    TreeviewComponent.prototype['updateFilterItems'] = function () {
      const _this = this;
      if (this.filterText !== '') {
        const filterItems_1 = [];
        const filterText_1 = this.filterText.toLowerCase();
        this.items.forEach(function (item) {
          const newItem = _this.filterItem(item, filterText_1);
          if (!_.isNil(newItem)) {
            filterItems_1.push(newItem);

            // Ensure overriden behaviour also applies during filtering
            if (!alreadyPatchedUpdateRefChecked && newItem.constructor.name === 'FilterTreeviewItem') {
              alreadyPatchedUpdateRefChecked = true;
              newItem.constructor.prototype['updateRefChecked'] = function () {
                this.children.forEach(function (child) {
                  if (child.constructor.name === 'FilterTreeviewItem') {
                    child.updateRefChecked();
                  }
                });
                this.refItem.checked = this.checked;
              };
            }
          }
        });
        this.filterItems = filterItems_1;
      } else {
        this.filterItems = this.items;
      }
      this.updateCheckedAll();
    };

    // Module ngx-treeview displays "All" selected wrongly, when top level items are selected during filtering
    // Override this behaviour so that "All" is only displayed when all items on all hierarchies are selected
    TreeviewComponent.prototype.onItemCheckedChange = function (item, checked) {
      if (this.allItem.checked !== checked) {
        let allItemChecked = true;
        for (let i = 0; i < this.items.length; i++) {
          if (!isCheckedRecursive(this.items[i])) {
            allItemChecked = false;
            break;
          }
        }
        if (this.allItem.checked !== allItemChecked) {
          this.allItem.checked = allItemChecked;
        }
      }
      if (item.constructor.name === 'FilterTreeviewItem') {
        item['updateRefChecked']();
      }
      this.raiseSelectedChange();
    };
    TreeviewComponent.prototype['updateCheckedAll'] = function () {
      let hasItemUnchecked = false;
      for (let i = 0; i < this.items.length; i++) {
        if (!isCheckedRecursive(this.items[i])) {
          hasItemUnchecked = true;
          break;
        }
      }
      if (this.allItem.checked === hasItemUnchecked) {
        this.allItem.checked = !hasItemUnchecked;
      }
    };

    function isCheckedRecursive(item: TreeviewItem): boolean {
      if (!item.checked) {
        return false;
      }

      if (!_.isNil(item.children)) {
        for (let i = 0; i < item.children.length; i++) {
          if (!isCheckedRecursive(item.children[i])) {
            return false;
          }
        }
      }

      return true;
    }

    this.treeSelectItems.push(new TreeviewItem({
      text: 'Austria', value: '1', checked: false, children: [{
        text: 'Styria', value: '1-1', checked: false, children: [{
          text: 'Graz', value: '1-1-1', checked: false
        }, {
          text: 'Mariazell', value: '1-1-2', checked: false
        }]
      }, {
        text: 'Tyrol', value: '1-2', checked: false, children: [{
          text: 'Oberndorf', value: '1-2-1', checked: false
        }]
      }]
    }));
    this.treeSelectItems.push(new TreeviewItem({
      text: 'United Kingdom', value: '2', checked: false, children: [{
        text: 'Cambridge', value: '2-1', checked: false
      }, {
        text: 'Edinburgh', value: '2-2', checked: false
      }, {
        text: 'Manchester', value: '2-3', checked: false
      }]
    }));
    this.treeSelectItems.push(new TreeviewItem({
      text: 'Hong Kong', value: '3', checked: false
    }));
    this.treeSelectItems.push(new TreeviewItem({
      text: 'Japan', value: '4', checked: false, children: [{
        text: 'Tokyo', value: '4-1', checked: false
      }]
    }));
  }

  treeItemOnSelect(event: UIEvent): void {
    console.log(event);
  }
}
