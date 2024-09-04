import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CustomNotificationComponent } from './custom-notification.component';

describe('CustomNotificationComponent', () => {
  let component: CustomNotificationComponent;
  let fixture: ComponentFixture<CustomNotificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CustomNotificationComponent ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomNotificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should not be visible if visible input is false', () => {
    component.visible = false;
    fixture.detectChanges(); // Ensure Angular has updated the DOM
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.notification')).toBeNull(); // No .notification element should be present
  });

  it('should emit closed event when close method is called', () => {
    spyOn(component.closed, 'emit');
    component.close();
    expect(component.closed.emit).toHaveBeenCalled();
  });
});
