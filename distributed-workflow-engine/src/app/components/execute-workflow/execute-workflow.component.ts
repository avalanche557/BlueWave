import { PerisitenceService } from './../../services/persistence/perisitence.service';
import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-execute-workflow',
  templateUrl: './execute-workflow.component.html',
  styleUrls: ['./execute-workflow.component.css']
})
export class ExecuteWorkflowComponent implements OnInit {

  constructor(private _formBuilder : FormBuilder,
  private persistenceService: PerisitenceService) { }
  response: any;
  ngOnInit() {
    this. response = this.persistenceService.triggerEngine();
    console.log(this.response);
    this.firstFormGroup = this._formBuilder.group({
      firstCtrl: ['', Validators.required]
    });
    this.secondFormGroup = this._formBuilder.group({
      secondCtrl: ['', Validators.required]
    });
  }
  isLinear = false;
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;

}
