import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getParents } from 'app/entities/parent/parent.reducer';
import { getEntities as getVaccinations } from 'app/entities/vaccination/vaccination.reducer';
import { createEntity, getEntity, reset, updateEntity } from './feedback.reducer';

export const FeedbackUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const parents = useAppSelector(state => state.parent.entities);
  const vaccinations = useAppSelector(state => state.vaccination.entities);
  const feedbackEntity = useAppSelector(state => state.feedback.entity);
  const loading = useAppSelector(state => state.feedback.loading);
  const updating = useAppSelector(state => state.feedback.updating);
  const updateSuccess = useAppSelector(state => state.feedback.updateSuccess);

  const handleClose = () => {
    navigate(`/feedback${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getParents({}));
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
    values.dateSubmitted = convertDateTimeToServer(values.dateSubmitted);

    const entity = {
      ...feedbackEntity,
      ...values,
      parent: parents.find(it => it.id.toString() === values.parent?.toString()),
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
      ? {
          dateSubmitted: displayDefaultDateTime(),
        }
      : {
          ...feedbackEntity,
          dateSubmitted: convertDateTimeFromServer(feedbackEntity.dateSubmitted),
          parent: feedbackEntity?.parent?.id,
          vaccination: feedbackEntity?.vaccination?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartVaxApp.feedback.home.createOrEditLabel" data-cy="FeedbackCreateUpdateHeading">
            <Translate contentKey="smartVaxApp.feedback.home.createOrEditLabel">Create or edit a Feedback</Translate>
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
                  id="feedback-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartVaxApp.feedback.messageText')}
                id="feedback-messageText"
                name="messageText"
                data-cy="messageText"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('smartVaxApp.feedback.sideEffects')}
                id="feedback-sideEffects"
                name="sideEffects"
                data-cy="sideEffects"
                type="text"
              />
              <ValidatedField
                label={translate('smartVaxApp.feedback.treatment')}
                id="feedback-treatment"
                name="treatment"
                data-cy="treatment"
                type="text"
              />
              <ValidatedField
                label={translate('smartVaxApp.feedback.dateSubmitted')}
                id="feedback-dateSubmitted"
                name="dateSubmitted"
                data-cy="dateSubmitted"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="feedback-parent"
                name="parent"
                data-cy="parent"
                label={translate('smartVaxApp.feedback.parent')}
                type="select"
              >
                <option value="" key="0" />
                {parents
                  ? parents.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="feedback-vaccination"
                name="vaccination"
                data-cy="vaccination"
                label={translate('smartVaxApp.feedback.vaccination')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/feedback" replace color="info">
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

export default FeedbackUpdate;
