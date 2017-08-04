import {Component} from '@angular/core';

import {TreeviewConfig, TreeviewItem} from 'ngx-treeview';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  private title: String = 'app';
  private treeSelectConfig: TreeviewConfig = TreeviewConfig.create({
    hasAllCheckBox: false,
    hasFilter: true,
    hasCollapseExpand: false
  });
  private treeSelectItems = new TreeviewItem({
    text: 'IT', value: 9, children: [
      {
        text: 'Programming', value: 91, children: [{
        text: 'Frontend', value: 911, children: [
          {text: 'Angular 1', value: 9111},
          {text: 'Angular 2', value: 9112},
          {text: 'ReactJS', value: 9113}
        ]
      }, {
        text: 'Backend', value: 912, children: [
          {text: 'C#', value: 9121},
          {text: 'Java', value: 9122},
          {text: 'Python', value: 9123, checked: false}
        ]
      }]
      },
      {
        text: 'Networking', value: 92, children: [
        {text: 'Internet', value: 921},
        {text: 'Security', value: 922}
      ]
      }
    ]
  });

  private treeItemOnSelect(event: UIEvent): void {
    console.log(event.target);
  }
}
