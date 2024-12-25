export enum PostStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
}

export interface Post {
    id: number;
    author: string;
    title: string;
    content: string;
    isConcept: boolean;
    status: PostStatus; 
    createdDate: string;
  }