import { Role } from "./role";

export class User {
  id: number | undefined;
  username: string | undefined;
  password: string | undefined | null;
  firstName: string | undefined;
  lastName: string | undefined;
  email: string | undefined;
  phone: string | undefined;
  imageUrl: string | undefined;
  roles: { name: Role }[] | undefined;
  approval: boolean | undefined;
}