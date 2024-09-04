import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'app-custom-notification',
  templateUrl: './custom-notification.component.html',
  styleUrls: ['./custom-notification.component.css']
})
export class CustomNotificationComponent {
  @Input() message: string = '';
  @Input() visible: boolean = false;
  @Output() closed = new EventEmitter<void>();

  close() {
    this.visible = false;
    this.closed.emit();
  }
}
