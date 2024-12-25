export interface Review {
    id: number;
    postId: number;
    author: string;
    content: string;
    isApproved: boolean;
    createdDate: string;
}