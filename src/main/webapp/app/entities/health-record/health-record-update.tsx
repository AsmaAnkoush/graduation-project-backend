import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './health-record.reducer';

export const HealthRecordUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const healthRecordEntity = useAppSelector(state => state.healthRecord.entity);
  const loading = useAppSelector(state => state.healthRecord.loading);
  const updating = useAppSelector(state => state.healthRecord.updating);
  const updateSuccess = useAppSelector(state => state.healthRecord.updateSuccess);

  const handleClose = () => {
    navigate(`/health-record${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }

    const entity = {
      ...healthRecordEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...healthRecordEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartVaxApp.healthRecord.home.createOrEditLabel" data-cy="HealthRecordCreateUpdateHeading">
            <Translate contentKey="smartVaxApp.healthRecord.home.createOrEditLabel">Create or edit a HealthRecord</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="health-record-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartVaxApp.healthRecord.sensitivity')}
                id="health-record-sensitivity"
                name="sensitivity"
                data-cy="sensitivity"
                type="text"
              />
              <ValidatedField
                label={translate('smartVaxApp.healthRecord.diabetes')}
                id="health-record-diabetes"
                name="diabetes"
                data-cy="diabetes"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('smartVaxApp.healthRecord.highBloodPressure')}
                id="health-record-highBloodPressure"
                name="highBloodPressure"
                data-cy="highBloodPressure"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('smartVaxApp.healthRecord.geneticDiseases')}
                id="health-record-geneticDiseases"
                name="geneticDiseases"
                data-cy="geneticDiseases"
                type="text"
              />
              <ValidatedField
                label={translate('smartVaxApp.healthRecord.bloodType')}
                id="health-record-bloodType"
                name="bloodType"
                data-cy="bloodType"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/health-record" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default HealthRecordUpdate;
