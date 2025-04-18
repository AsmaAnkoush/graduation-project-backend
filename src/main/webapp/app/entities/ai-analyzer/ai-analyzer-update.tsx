import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities as getFeedbacks } from 'app/entities/feedback/feedback.reducer';
import { createEntity, getEntity, reset, updateEntity } from './ai-analyzer.reducer';

export const AIAnalyzerUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const feedbacks = useAppSelector(state => state.feedback.entities);
  const aIAnalyzerEntity = useAppSelector(state => state.aIAnalyzer.entity);
  const loading = useAppSelector(state => state.aIAnalyzer.loading);
  const updating = useAppSelector(state => state.aIAnalyzer.updating);
  const updateSuccess = useAppSelector(state => state.aIAnalyzer.updateSuccess);

  const handleClose = () => {
    navigate(`/ai-analyzer${location.search}`);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getFeedbacks({}));
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
      ...aIAnalyzerEntity,
      ...values,
      feedback: feedbacks.find(it => it.id.toString() === values.feedback?.toString()),
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
          ...aIAnalyzerEntity,
          feedback: aIAnalyzerEntity?.feedback?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="smartVaxApp.aIAnalyzer.home.createOrEditLabel" data-cy="AIAnalyzerCreateUpdateHeading">
            <Translate contentKey="smartVaxApp.aIAnalyzer.home.createOrEditLabel">Create or edit a AIAnalyzer</Translate>
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
                  id="ai-analyzer-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('smartVaxApp.aIAnalyzer.analysisResult')}
                id="ai-analyzer-analysisResult"
                name="analysisResult"
                data-cy="analysisResult"
                type="text"
              />
              <ValidatedField
                id="ai-analyzer-feedback"
                name="feedback"
                data-cy="feedback"
                label={translate('smartVaxApp.aIAnalyzer.feedback')}
                type="select"
              >
                <option value="" key="0" />
                {feedbacks
                  ? feedbacks.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ai-analyzer" replace color="info">
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

export default AIAnalyzerUpdate;
