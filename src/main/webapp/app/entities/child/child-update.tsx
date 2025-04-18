import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getHealthRecords } from 'app/entities/health-record/health-record.reducer';
import { getEntities as getParents } from 'app/entities/parent/parent.reducer';
import { createEntity, getEntity, reset, updateEntity } from './child.reducer';

export const ChildUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const healthRecords = useAppSelector(state => state.healthRecord.entities);
  const parents = useAppSelector(state => state.parent.entities);
  const childEntity = useAppSelector(state => state.child.entity);
  const loading = useAppSelector(state => state.child.loading);
  const updating = useAppSelector(state => state.child.updating);
  const updateSuccess = useAppSelector(state => state.child.updateSuccess);

  const handleClose = () => {
    navigate(`/child${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getHealthRecords({}));
    dispatch(getParents({}));
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
    if (values.weight !== undefined && typeof values.weight !== 'number') {
      values.weight = Number(values.weight);
    }
    if (values.height !== undefined && typeof values.height !== 'number') {
      values.height = Number(values.height);
    }

    const entity = {
      ...childEntity,
      ...values,
      healthRecord: healthRecords.find(it => it.id.toString() === values.healthRecord?.toString()),
      parent: parents.find(it => it.id.toString() === values.parent?.toString()),
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
          ...childEntity,
          healthRecord: childEntity?.healthRecord?.id,
          parent: childEntity?.parent?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartVaxApp.child.home.createOrEditLabel" data-cy="ChildCreateUpdateHeading">
            <Translate contentKey="smartVaxApp.child.home.createOrEditLabel">Create or edit a Child</Translate>
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
                  id="child-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartVaxApp.child.name')}
                id="child-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartVaxApp.child.dob')}
                id="child-dob"
                name="dob"
                data-cy="dob"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('smartVaxApp.child.weight')} id="child-weight" name="weight" data-cy="weight" type="text" />
              <ValidatedField label={translate('smartVaxApp.child.height')} id="child-height" name="height" data-cy="height" type="text" />
              <ValidatedField
                id="child-healthRecord"
                name="healthRecord"
                data-cy="healthRecord"
                label={translate('smartVaxApp.child.healthRecord')}
                type="select"
              >
                <option value="" key="0" />
                {healthRecords
                  ? healthRecords.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="child-parent" name="parent" data-cy="parent" label={translate('smartVaxApp.child.parent')} type="select">
                <option value="" key="0" />
                {parents
                  ? parents.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/child" replace color="info">
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

export default ChildUpdate;
