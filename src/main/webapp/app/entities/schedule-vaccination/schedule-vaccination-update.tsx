import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getChildren } from 'app/entities/child/child.reducer';
import { getEntities as getVaccinations } from 'app/entities/vaccination/vaccination.reducer';
import { createEntity, getEntity, reset, updateEntity } from './schedule-vaccination.reducer';

export const ScheduleVaccinationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const children = useAppSelector(state => state.child.entities);
  const vaccinations = useAppSelector(state => state.vaccination.entities);
  const scheduleVaccinationEntity = useAppSelector(state => state.scheduleVaccination.entity);
  const loading = useAppSelector(state => state.scheduleVaccination.loading);
  const updating = useAppSelector(state => state.scheduleVaccination.updating);
  const updateSuccess = useAppSelector(state => state.scheduleVaccination.updateSuccess);

  const handleClose = () => {
    navigate(`/schedule-vaccination${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getChildren({}));
    dispatch(getVaccinations({}));
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
      ...scheduleVaccinationEntity,
      ...values,
      child: children.find(it => it.id.toString() === values.child?.toString()),
      vaccination: vaccinations.find(it => it.id.toString() === values.vaccination?.toString()),
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
          ...scheduleVaccinationEntity,
          child: scheduleVaccinationEntity?.child?.id,
          vaccination: scheduleVaccinationEntity?.vaccination?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartVaxApp.scheduleVaccination.home.createOrEditLabel" data-cy="ScheduleVaccinationCreateUpdateHeading">
            <Translate contentKey="smartVaxApp.scheduleVaccination.home.createOrEditLabel">Create or edit a ScheduleVaccination</Translate>
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
                  id="schedule-vaccination-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartVaxApp.scheduleVaccination.scheduledDate')}
                id="schedule-vaccination-scheduledDate"
                name="scheduledDate"
                data-cy="scheduledDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartVaxApp.scheduleVaccination.status')}
                id="schedule-vaccination-status"
                name="status"
                data-cy="status"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="schedule-vaccination-child"
                name="child"
                data-cy="child"
                label={translate('smartVaxApp.scheduleVaccination.child')}
                type="select"
              >
                <option value="" key="0" />
                {children
                  ? children.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="schedule-vaccination-vaccination"
                name="vaccination"
                data-cy="vaccination"
                label={translate('smartVaxApp.scheduleVaccination.vaccination')}
                type="select"
              >
                <option value="" key="0" />
                {vaccinations
                  ? vaccinations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/schedule-vaccination" replace color="info">
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

export default ScheduleVaccinationUpdate;
