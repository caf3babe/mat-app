import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CarsItemComponent} from './cars-item.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {BookCarComponent} from "../book-car/book-car.component";
import {Car} from "../../../model/car";
import {CarService} from "../../../api/car.service";
import {InputSearch} from "../InputSearch";
import {Currency} from "../../../model/currency";
import {buildCar} from "../../../mock/order-mock/buildCar";

describe('CarsItemComponent', () => {
  let component: CarsItemComponent;
  let fixture: ComponentFixture<CarsItemComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CarsItemComponent, BookCarComponent],
      imports: [HttpClientTestingModule],
      providers: [CarService]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CarsItemComponent);
    component = fixture.componentInstance;
    const mockCar = buildCar()
    component.car = mockCar;

    const mockInputSearch: InputSearch = {
      selectedDateFrom: "this.dateToday",
      selectedDateTo: "this.dateTodayPlusSeven",
      selectedCurrencySymbol: Currency.SymbolEnum.Usd
    };

    component.inputSearch = mockInputSearch;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should set visibility on true', () => {
    component.bookCar();
    expect(component.visibilityBookCar).toBeTruthy();
  });

  it('should calculate price',()=>{
    component.inputSearch.selectedDateFrom="2022-01-01";
    component.inputSearch.selectedDateTo="2022-01-02";
    component.car.price=20000;
    return expect(component.getPrice()).toBe("400.00");
  });

});
