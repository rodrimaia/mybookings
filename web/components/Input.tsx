import { ErrorMessage, Field } from "formik";

export const Input = ({ label, name, elementType }) => (
  <div className="">
    <label className="block uppercase tracking-wide text-grey-darker text-xs font-bold mb-2">
      {label}
    </label>
    <Field
      required
      type={elementType}
      name={name}
      placeholder={label}
      className="appearance-none block w-full bg-grey-lighter text-grey-darker border border-red rounded py-3 px-4 mb-3"
    />
    <ErrorMessage name={name} component="div" />
  </div>
);