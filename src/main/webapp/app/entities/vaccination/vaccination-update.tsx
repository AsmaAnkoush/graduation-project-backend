import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './vaccination.reducer';

export const VaccinationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const vaccinationEntity = useAppSelector(state => state.vaccination.entity);
  const loading = useAppSelector(state => state.vaccination.loading);
  const updating = useAppSelector(state => state.vaccination.updating);
  const updateSuccess = useAppSelector(state => state.vaccination.updateSuccess);

  const handleClose = () => {
    navigate(`/vaccination${location.search}`);
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
    if (values.targetAge !== undefined && typeof values.targetAge !== 'number') {
      values.targetAge = Number(values.targetAge);
    }

    const entity = {
      ...vaccinationEntity,
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
          ...vaccinationEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartVaxApp.vaccination.home.createOrEditLabel" data-cy="VaccinationCreateUpdateHeading">
            <Translate contentKey="smartVaxApp.vaccination.home.createOrEditLabel">Create or edit a Vaccination</Translate>
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
                  id="vaccination-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartVaxApp.vaccination.name')}
                id="vaccination-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartVaxApp.vaccination.type')}
                id="vaccination-type"
                name="type"
                data-cy="type"
                type="text"
              />
              <ValidatedField
                label={translate('smartVaxApp.vaccination.dateGiven')}
                id="vaccination-dateGiven"
                name="dateGiven"
                data-cy="dateGiven"
                type="date"
              />
              <ValidatedField
                label={translate('smartVaxApp.vaccination.sideEffects')}
                id="vaccination-sideEffects"
                name="sideEffects"
                data-cy="sideEffects"
                type="text"
              />
              <ValidatedField
                label={translate('smartVaxApp.vaccination.targetAge')}
                id="vaccination-targetAge"
                name="targetAge"
                data-cy="targetAge"
                type="text"
              />
              <ValidatedField
                label={translate('smartVaxApp.vaccination.status')}
                id="vaccination-status"
                name="status"
                data-cy="status"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartVaxApp.vaccination.treatment')}
                id="vaccination-treatment"
                name="treatment"
                data-cy="treatment"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/vaccination" replace color="info">
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

export default VaccinationUpdate;
